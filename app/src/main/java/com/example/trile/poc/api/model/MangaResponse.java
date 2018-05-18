package com.example.trile.poc.api.model;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MangaResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private ResponseData data;

    public int getCode() {
        return code;
    }

    public ResponseData getData() {
        return data;
    }

    public class ResponseData {

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
}
