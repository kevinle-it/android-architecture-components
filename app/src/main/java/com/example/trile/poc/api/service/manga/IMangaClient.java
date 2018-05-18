package com.example.trile.poc.api.service.manga;

import com.example.trile.poc.api.model.MangaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IMangaClient {
    @GET("catalog")
    Call<MangaResponse> getAllMangaItems(@Query("msid") int msid,
                                         @Query("country") String country);
}
