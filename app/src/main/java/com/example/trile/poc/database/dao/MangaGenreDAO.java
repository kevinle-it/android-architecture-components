package com.example.trile.poc.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.trile.poc.database.entity.MangaGenreEntity;
import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * @author trile
 * @since 6/11/18 at 20:11
 */
@Dao
public interface MangaGenreDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MangaGenreEntity> mangaGenres);

    @Query(
            "SELECT * " +
            "FROM MangaItemEntity " +
            "WHERE Id IN (SELECT MangaId " +
                         "FROM MangaGenreEntity " +
                         "WHERE GenreId IN (:includeGenres) EXCEPT SELECT MangaId " +
                                                                  "FROM MangaGenreEntity " +
                                                                  "WHERE GenreId IN (:excludeGenres)" +
                        ") " +
            "ORDER BY " +
            "CASE :orderBy " +
            "WHEN 'Name' THEN Name " +
            "WHEN 'Rank' THEN Rank " +
            "END ASC " +
            "LIMIT :loadCount OFFSET :startPosition"
    )
    List<MangaItemEntity> filterManga(List<Integer> includeGenres, List<Integer> excludeGenres,
                                      int startPosition, int loadCount, String orderBy);

    @Query(
            "SELECT COUNT(*) " +
            "FROM MangaItemEntity " +
            "WHERE Id IN (SELECT MangaId " +
                         "FROM MangaGenreEntity " +
                         "WHERE GenreId IN (:includeGenres) EXCEPT SELECT MangaId " +
                                                                  "FROM MangaGenreEntity " +
                                                                  "WHERE GenreId IN (:excludeGenres)" +
                        ")"
    )
    int countFilteredManga(List<Integer> includeGenres, List<Integer> excludeGenres);
}
