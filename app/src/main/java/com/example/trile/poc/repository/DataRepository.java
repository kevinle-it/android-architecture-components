package com.example.trile.poc.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<MangaItemEntity>> mObservableMangaItems;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableMangaItems = new MediatorLiveData<>();

        mObservableMangaItems.addSource(mDatabase.mangaItemDAO().loadAllMangaItems(),
                mangaItemEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableMangaItems.postValue(mangaItemEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<MangaItemEntity>> getMangaItems() {
        return mObservableMangaItems;
    }

    public LiveData<MangaItemEntity> loadMangaItem(final int mangaItemId) {
        return mDatabase.mangaItemDAO().loadMangaItem(mangaItemId);
    }
}
