package com.example.trile.poc.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.content.Context;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.api.service.manga.DiscoverAllBoundaryCallback;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.repository.paging.MangaItemDataSource;
import com.example.trile.poc.repository.paging.MangaItemDataSourceFactory;
import com.example.trile.poc.utils.InjectorUtils;

import java.util.List;

import io.reactivex.Observable;

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

        LiveData<List<MangaItemEntity>> networkData =
                mMangaNetworkDataSource.getDownloadedMangaItems();
        networkData.observeForever(newMangaItemsFromNetwork ->
                mExecutors.diskIO().execute(() ->
                        mDatabase.mangaItemDAO().insertAll(newMangaItemsFromNetwork)
                )
        );
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
    public Observable<PagedList<MangaItemEntity>> getAllMangaItems(Context context, String orderBy) {
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

        return new RxPagedListBuilder(
                mangaItemDataSourceFactory,
                pagedListConfig
        ).setBoundaryCallback(
                new DiscoverAllBoundaryCallback(InjectorUtils.provideNetworkDataSource())
        ).buildObservable();
    }

    public MangaItemEntity loadMangaItemById(final int mangaItemId) {
        initializeData();
        return mDatabase.mangaItemDAO().loadMangaItem(mangaItemId);
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        // This method call triggers Manga Rock to create its task to synchronize manga item data
        // periodically.
//        RetrofitClient.scheduleRecurringFetchMangaSync();

        mExecutors.diskIO().execute(() -> {
            if (isFetchNeeded()) {
                startFetchAllMangaItems();
            }
        });
    }

    /**
     * Checks if there is any manga for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded() {
        return (!mDatabase.mangaItemDAO().isExistAnyManga());
    }

    /**
     * Network related operation
     */
    private void startFetchAllMangaItems() {
        mMangaNetworkDataSource.startFetchAllMangaItems();
    }
}
