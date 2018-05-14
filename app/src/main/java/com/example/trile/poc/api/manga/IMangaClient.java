package com.example.trile.poc.api.manga;

import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IMangaClient {
    @GET("catalog")
    Call<List<MangaItemEntity>> getAllMangas(@Query("msid") int msid,
                                             @Query("country") String country);
}
