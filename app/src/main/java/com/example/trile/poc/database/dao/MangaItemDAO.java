package com.example.trile.poc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * @author trile
 * @since 5/22/18 at 14:07
 */
@Dao
public interface MangaItemDAO {
    @Query("SELECT * FROM MangaItemEntity LIMIT 500")
    LiveData<List<MangaItemEntity>> loadAllMangaItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MangaItemEntity> mangaItems);

    @Query("SELECT * FROM MangaItemEntity WHERE Id = :mangaItemId")
    LiveData<MangaItemEntity> loadMangaItem(int mangaItemId);

    @Query("SELECT EXISTS (SELECT Id FROM MangaItemEntity LIMIT 1)")
    boolean isExistAnyManga();

//    @Query("SELECT * FROM MangaItemEntity WHERE Favorited = 1")
//    LiveData<List<MangaItemEntity>> loadFavoriteMangas();
//
//    @Query("SELECT * FROM MangaItemEntity WHERE ReadElapsedTime > 0")
//    LiveData<List<MangaItemEntity>> loadRecentMangas();
//
//    @Query("SELECT * FROM MangaItemEntity WHERE NumChapterDownloaded > 0")
//    LiveData<List<MangaItemEntity>> loadDownloadedMangas();
}
