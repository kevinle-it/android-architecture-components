package com.example.trile.poc.api.model;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author trile
 * @since 5/22/18 at 14:00
 */
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

    /**
     * avoid using non-static inner class
     * watch here: https://www.youtube.com/watch?v=_CruQY55HOk
     */
    public static class ResponseData {

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
