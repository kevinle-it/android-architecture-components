package com.example.trile.poc.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author trile
 * @since 6/11/18 at 11:52
 */
@Entity
public class GenreEntity implements com.example.trile.poc.database.model.Genre {

    @PrimaryKey
    private int Id;

    private String Genre;

    public GenreEntity() {
    }

    public GenreEntity(int id, String genre) {
        Id = id;
        Genre = genre;
    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public String getGenre() {
        return Genre;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }
}
