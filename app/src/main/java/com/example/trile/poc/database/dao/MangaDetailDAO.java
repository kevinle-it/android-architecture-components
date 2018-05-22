package com.example.trile.poc.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.trile.poc.database.entity.MangaDetailEntity;

import java.util.List;

/**
 * @author trile
 * @since 5/22/18 at 14:06
 */
@Dao
public interface MangaDetailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MangaDetailEntity> mangaDetailEntities);

    @Query("SELECT * FROM MangaDetailEntity WHERE Id = :mangaDetailId")
    LiveData<MangaDetailEntity> loadMangaDetail(int mangaDetailId);
}
