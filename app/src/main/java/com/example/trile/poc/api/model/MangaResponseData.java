package com.example.trile.poc.api.model;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author trile
 * @since 5/22/18 at 12:27
 */
public class MangaResponseData {
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("list")
    private List<MangaItemEntity> mangaItems;

    public long getTimestamp() {
        return timestamp;
    }

    public List<MangaItemEntity> getMangaItems() {
        return mangaItems;
    }
}
