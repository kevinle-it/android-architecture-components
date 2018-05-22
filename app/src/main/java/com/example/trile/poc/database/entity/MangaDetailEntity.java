package com.example.trile.poc.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.trile.poc.database.model.MangaDetail;

/**
 * @author trile
 * @since 5/22/18 at 14:07
 */
@Entity(foreignKeys = {
            @ForeignKey( entity = MangaItemEntity.class,
                    parentColumns = "Id",
                    childColumns = "Id",
                    onDelete = ForeignKey.CASCADE)
            }
        )
public class MangaDetailEntity implements MangaDetail {

    @PrimaryKey
    private int Id;

    private String Description;

    public MangaDetailEntity() {
    }

    public MangaDetailEntity(int id, String description) {
        Id = id;
        Description = description;
    }

    public MangaDetailEntity(MangaDetail mangaDetail) {
        this.Id = mangaDetail.getId();
        this.Description = mangaDetail.getDescription();
    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public String getDescription() {
        return Description;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
