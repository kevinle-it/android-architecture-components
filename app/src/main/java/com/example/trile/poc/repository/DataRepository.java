package com.example.trile.poc.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.content.Context;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.entity.GenreEntity;
import com.example.trile.poc.database.entity.MangaGenreEntity;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.database.model.MangaItemsAndGenres;
import com.example.trile.poc.repository.paging.discover.all.DiscoverAllBoundaryCallback;
import com.example.trile.poc.repository.paging.discover.all.MangaItemDataSource;
import com.example.trile.poc.repository.paging.discover.all.MangaItemDataSourceFactory;
import com.example.trile.poc.repository.paging.search.genre.FilterByGenreResultDataSourceFactory;
import com.example.trile.poc.utils.InjectorUtils;
import com.example.trile.poc.utils.Objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Single Data Source of Truth of this whole Application.
 *
 * @author trile
 * @since 5/22/18 at 11:54
 */
public class DataRepository {
    public static final String TAG = DataRepository.class.getSimpleName();

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private final MangaNetworkDataSource mMangaNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private DataRepository(final AppDatabase database,
                           final MangaNetworkDataSource mangaNetworkDataSource,
                           final AppExecutors executors) {
        mDatabase = database;
        mMangaNetworkDataSource = mangaNetworkDataSource;
        mExecutors = executors;
    }

    /**
     * Get the singleton for this class
     */
    public static DataRepository getInstance(final AppDatabase database,
                                             final MangaNetworkDataSource mangaNetworkDataSource,
                                             final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database, mangaNetworkDataSource, executors);
                }
            }
        }
        return sInstance;
    }

    /**
     * Expose this LiveData (list of all manga items from the database) for others
     * and get notified when the local data changes.
     */
    public Observable<PagedList<MangaItemEntity>> getAllMangaItems(Context context,
                                                                   String orderBy,
                                                                   CompositeDisposable compositeDisposable) {
//        initializeData();     // Don't need to initialize data anymore thanks to the handling of
                                // Paging Library's BoundaryCallback fetching new Manga Items
                                // automatically from Server on Zero Item/on Item at End of
                                // RecyclerView loaded.

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)    /**
                                                 * PagedList will present null placeholders
                                                 * for not-yet-loaded content.
                                                 * This means its DataSource
                                                 * (the {@link MangaItemDataSource}) can count all
                                                 * unloaded items (so that the number of nulls
                                                 * to present is known).
                                                 */
                .setPrefetchDistance(60)
                .setPageSize(20)
                .build();

        MangaItemDataSourceFactory mangaItemDataSourceFactory = new MangaItemDataSourceFactory(
                context.getApplicationContext(), orderBy
        );

        DiscoverAllBoundaryCallback discoverAllBoundaryCallback =
                new DiscoverAllBoundaryCallback(
                        this,
                        InjectorUtils.provideNetworkDataSource(),
                        compositeDisposable
                );

        if (!mInitialized) {
            LiveData<MangaItemsAndGenres> networkData =
                    discoverAllBoundaryCallback.getMangaItemsAndGenres();
            networkData.observeForever(newMangaItemsAndGenresFromNetwork -> {

                HashMap<Integer, String> newGenresFromNetwork =
                        newMangaItemsAndGenresFromNetwork.getGenres();

                List<GenreEntity> genres = new ArrayList<>();
                if (Objects.nonNull(newGenresFromNetwork) && newGenresFromNetwork.size() > 0) {

                    for (Map.Entry<Integer, String> entry : newGenresFromNetwork.entrySet()) {
                        genres.add(
                                new GenreEntity(
                                        entry.getKey(),
                                        entry.getValue()
                                )
                        );
                    }
                    if (genres.size() > 0) {
                        Collections.sort(
                                genres,
                                (genre1, genre2) ->
                                        genre1.getGenre()
                                                .compareTo(genre2.getGenre())
                        );
                    }
                }

                List<MangaItemEntity> newMangaItemsFromNetwork =
                        newMangaItemsAndGenresFromNetwork.getMangaItems();

                List<MangaGenreEntity> mangaGenres = new ArrayList<>();
                if (Objects.nonNull(newMangaItemsFromNetwork) && newMangaItemsFromNetwork.size() > 0) {
                    for (MangaItemEntity item : newMangaItemsAndGenresFromNetwork.getMangaItems()) {
                        for (int genreId : item.getGenres()) {
                            mangaGenres.add(new MangaGenreEntity(item.getId(), genreId));
                        }
                    }
                }
                if (Objects.nonNull(newMangaItemsFromNetwork)
                        && Objects.nonNull(newGenresFromNetwork)) {
                    Completable.fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            mDatabase.mangaItemDAO().insertAll(newMangaItemsFromNetwork);
                            mDatabase.genreDAO().insertAll(genres);
                            mDatabase.mangaGenreDAO().insertAll(mangaGenres);
                        }
                    }).subscribeOn(Schedulers.io()).subscribe();
                }
            });
            mInitialized = true;
        }

        //noinspection unchecked
        return new RxPagedListBuilder(
                mangaItemDataSourceFactory,
                pagedListConfig
        ).setBoundaryCallback(
                discoverAllBoundaryCallback
        ).buildObservable();
    }

    public Observable<List<GenreEntity>> getAllGenres() {
        return Observable.defer(() ->   // Use defer to postpone the running of
                                        // isFetchAllGenresNeeded().
                Observable.just(isFetchAllGenresNeeded())
                        .map(isFetchNeeded -> {
                            if (isFetchNeeded) {
                                HashMap<Integer, String> newGenresFromNetwork =
                                        startFetchAllGenres();

                                if (newGenresFromNetwork != null) {
                                    List<GenreEntity> genres = new ArrayList<>();

                                    for (Map.Entry<Integer, String> entry :
                                            newGenresFromNetwork.entrySet()) {
                                        genres.add(
                                                new GenreEntity(
                                                        entry.getKey(),
                                                        entry.getValue()
                                                )
                                        );
                                    }
                                    if (genres.size() > 0) {
                                        Collections.sort(
                                                genres,
                                                (genre1, genre2) ->
                                                        genre1.getGenre()
                                                                .compareTo(genre2.getGenre())
                                        );
                                    }
                                    mDatabase.genreDAO().insertAll(genres);
                                }
                            }
                            return mDatabase.genreDAO().loadAllGenres();
                        })
        );
    }

    public Observable<PagedList<MangaItemEntity>> filterMangaByGenre(Context context,
                                                                     List<Integer> includeGenres,
                                                                     List<Integer> excludeGenres,
                                                                     String orderBy) {
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPrefetchDistance(60)
                .setPageSize(20)
                .build();

        FilterByGenreResultDataSourceFactory filterByGenreResultDataSourceFactory =
                new FilterByGenreResultDataSourceFactory(
                        context.getApplicationContext(),
                        includeGenres,
                        excludeGenres,
                        orderBy
                );

        //noinspection unchecked
        return new RxPagedListBuilder(
                filterByGenreResultDataSourceFactory,
                pagedListConfig
        ).buildObservable();
    }

    /**
     * Checks if there is any manga for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */
    public boolean isFetchAllMangaItemsNeeded() {
        return (!mDatabase.mangaItemDAO().isExistAnyManga());
    }

    public boolean isFetchAllGenresNeeded() {
        return (!mDatabase.genreDAO().isExistAnyGenre());
    }

    /**
     * Network related operation
     */
    private HashMap<Integer, String> startFetchAllGenres() throws IOException {
        return mMangaNetworkDataSource.startFetchAllGenres();
    }
}
