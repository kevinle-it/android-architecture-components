package com.example.trile.poc.api.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.api.service.manga.MangaClient;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

public class MangaNetworkDataSource {

    private static MangaNetworkDataSource sInstance;
    private final Context mContext;

    // LiveData storing the latest downloaded manga items
    private final MutableLiveData<List<MangaItemEntity>> mDownloadedMangaItems;

    public MangaNetworkDataSource(final Context context) {
        mContext = context;
        mDownloadedMangaItems = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static MangaNetworkDataSource getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (MangaNetworkDataSource.class) {
                if (sInstance == null) {
                    sInstance = new MangaNetworkDataSource(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<MangaItemEntity>> getDownloadedMangaItems() {
        return mDownloadedMangaItems;
    }

    public void startFetchAllMangaItems() {
        MangaClient.getAllMangaItems(mDownloadedMangaItems);
    }
}
