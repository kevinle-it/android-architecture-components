package com.example.trile.poc.repository.paging.discover.all;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.database.model.MangaItemsAndGenres;
import com.example.trile.poc.repository.DataRepository;
import com.example.trile.poc.utils.InjectorUtils;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author trile
 * @since 5/30/18 at 12:41
 */
public class DiscoverAllBoundaryCallback extends PagedList.BoundaryCallback<MangaItemEntity> {

    private final DataRepository mRepository;
    private final MangaNetworkDataSource mMangaNetworkDataSource;
    private final MutableLiveData<MangaItemsAndGenres> mMangaItemsAndGenres;
    private final CompositeDisposable mCompositeDisposable;

    public DiscoverAllBoundaryCallback(DataRepository repository,
                                       MangaNetworkDataSource mangaNetworkDataSource,
                                       CompositeDisposable compositeDisposable) {
        mRepository = repository;
        mMangaNetworkDataSource = mangaNetworkDataSource;
        mMangaItemsAndGenres = new MutableLiveData<>();
        mCompositeDisposable = compositeDisposable;
    }

    public LiveData<MangaItemsAndGenres> getMangaItemsAndGenres() {
        return mMangaItemsAndGenres;
    }

    @Override
    public void onZeroItemsLoaded() {
        mCompositeDisposable.add(
                Single.zip(
                        Single.defer(() ->
                                Single.just(mRepository.isFetchAllMangaItemsNeeded())
                                        .map(isFetchNeeded -> {
                                            if (isFetchNeeded) {
                                                try {
                                                    return mMangaNetworkDataSource.startFetchAllMangaItems();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            return null;
                                        })
                        ),
                        Single.defer(() ->
                                Single.just(mRepository.isFetchAllGenresNeeded())
                                        .map(isFetchNeeded -> {
                                            if (isFetchNeeded) {
                                                try {
                                                    return mMangaNetworkDataSource.startFetchAllGenres();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            return null;
                                        })
                        ),
                        (mangaItems, genres) -> new MangaItemsAndGenres(mangaItems, genres)
                ).subscribeOn(
                        Schedulers.from(InjectorUtils.provideAppExecutors().networkIO())
                ).observeOn(
                        Schedulers.io()
                ).subscribe(mMangaItemsAndGenres::postValue)
        );
    }

    @Override
    public void onItemAtEndLoaded(@NonNull MangaItemEntity itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);     // Method has not yet implemented because the
                                                // current version of Manga Rock Server returns
                                                // all Manga at once.
    }
}
