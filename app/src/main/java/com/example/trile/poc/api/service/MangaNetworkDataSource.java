package com.example.trile.poc.api.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.trile.poc.api.service.manga.MangaClient;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * Provides an API for handling all network operations.
 *
 * @author trile
 * @since 5/22/18 at 11:36
 */
public class MangaNetworkDataSource {

    private static MangaNetworkDataSource sInstance;

    // LiveData storing the latest downloaded manga items
    private final MutableLiveData<List<MangaItemEntity>> mDownloadedMangaItems;

    public MangaNetworkDataSource() {
        mDownloadedMangaItems = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static MangaNetworkDataSource getInstance() {
        if (sInstance == null) {
            synchronized (MangaNetworkDataSource.class) {
                if (sInstance == null) {
                    sInstance = new MangaNetworkDataSource();
                }
            }
        }
        return sInstance;
    }

    /**
     * Expose this LiveData (list of downloaded manga items) for others to observe the changes
     * on fetching manga items progress.
     *
     * @return Live Data for others to observe the changes.
     */
    public LiveData<List<MangaItemEntity>> getDownloadedMangaItems() {
        return mDownloadedMangaItems;
    }

    /**
     * Start to Fetch all manga items from Manga Rock server using Retrofit
     * and post the downloaded manga items to all observers.
     */
    public void startFetchAllMangaItems() {
        MangaClient.getAllMangaItems(mDownloadedMangaItems);
    }
}
