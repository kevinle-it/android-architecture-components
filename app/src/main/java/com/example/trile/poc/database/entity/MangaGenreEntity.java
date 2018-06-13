package com.example.trile.poc.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import com.example.trile.poc.database.model.MangaGenre;

/**
 * Represent Many-to-Many Relationship between MangaItemWEntity & GenreEntity.
 * One Manga may belong to one or more genres, and one genre may belong to one or more mangas.
 * @author trile
 * @since 6/11/18 at 19:37
 */
@Entity(
        primaryKeys = {"MangaId", "GenreId"},
        foreignKeys = {
                @ForeignKey(
                        entity = MangaItemEntity.class,
                        parentColumns = "Id",
                        childColumns = "MangaId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = GenreEntity.class,
                        parentColumns = "Id",
                        childColumns = "GenreId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "GenreId")}
)
public class MangaGenreEntity implements MangaGenre {

    private int MangaId;

    private int GenreId;

    public MangaGenreEntity() {
    }

    public MangaGenreEntity(int mangaId, int genreId) {
        MangaId = mangaId;
        GenreId = genreId;
    }

    @Override
    public int getMangaId() {
        return MangaId;
    }

    @Override
    public int getGenreId() {
        return GenreId;
    }

    public void setMangaId(int mangaId) {
        MangaId = mangaId;
    }

    public void setGenreId(int genreId) {
        GenreId = genreId;
    }
}
