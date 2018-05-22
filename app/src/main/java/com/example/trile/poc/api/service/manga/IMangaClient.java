package com.example.trile.poc.api.service.manga;

import com.example.trile.poc.api.model.MangaResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit caller interface.
 *
 * @author trile
 * @since 5/22/18 at 13:59
 */
public interface IMangaClient {
    @GET("catalog")
    Call<MangaResponse> getAllMangaItems(@Query("msid") int msid,
                                         @Query("country") String country);
}
