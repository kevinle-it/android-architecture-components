package com.example.trile.poc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

@Dao
public interface MangaItemDAO {
    @Query("SELECT * FROM MangaItemEntity")
    LiveData<List<MangaItemEntity>> loadAllMangaItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MangaItemEntity> mangaItems);

    @Query("SELECT * FROM MangaItemEntity WHERE Id = :mangaItemId")
    LiveData<MangaItemEntity> loadMangaItem(int mangaItemId);

    @Query("SELECT * FROM MangaItemEntity WHERE IsFavorited = 1")
    LiveData<List<MangaItemEntity>> loadFavoriteMangas();

    @Query("SELECT * FROM MangaItemEntity WHERE ReadElapsedTime > 0")
    LiveData<List<MangaItemEntity>> loadRecentMangas();

    @Query("SELECT * FROM MangaItemEntity WHERE NumChapterDownloaded > 0")
    LiveData<List<MangaItemEntity>> loadDownloadedMangas();
}
