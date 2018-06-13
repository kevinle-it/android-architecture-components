package com.example.trile.poc.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * @author trile
 * @since 6/11/18 at 11:22
 */
public class GenreResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private List<ResponseData> data;

    public int getCode() {
        return code;
    }

    public List<ResponseData> getData() {
        return data;
    }

    public static class ResponseData {

        @SerializedName("msid")
        private long msid;
        @SerializedName("categories")
        private HashMap<Integer, String> MangaGenres;

        public long getMsid() {
            return msid;
        }

        public HashMap<Integer, String> getMangaGenres() {
            return MangaGenres;
        }
    }
}
