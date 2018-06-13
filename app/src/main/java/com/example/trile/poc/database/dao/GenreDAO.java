package com.example.trile.poc.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.trile.poc.database.entity.GenreEntity;

import java.util.List;

/**
 * @author trile
 * @since 6/11/18 at 11:56
 */
@Dao
public interface GenreDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GenreEntity> genres);

    @Query("SELECT * FROM GenreEntity ORDER BY Genre ASC")
    List<GenreEntity> loadAllGenres();

    @Query("SELECT EXISTS (SELECT Id FROM GenreEntity LIMIT 1)")
    boolean isExistAnyGenre();
}
