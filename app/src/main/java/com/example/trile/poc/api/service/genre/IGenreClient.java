package com.example.trile.poc.api.service.genre;

import com.example.trile.poc.api.model.GenreResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit caller interface.
 *
 * @author trile
 * @since 6/11/18 at 11:30
 */
public interface IGenreClient {
    @GET("sources")
    Call<GenreResponse> getGenres(@Query("country") String country);
}
