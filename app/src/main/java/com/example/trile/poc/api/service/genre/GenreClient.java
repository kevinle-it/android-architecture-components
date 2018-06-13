package com.example.trile.poc.api.service.genre;

import android.support.annotation.Nullable;

import com.example.trile.poc.Constants;
import com.example.trile.poc.api.model.GenreResponse;
import com.example.trile.poc.api.service.RetrofitClient;
import com.example.trile.poc.utils.Objects;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author trile
 * @since 6/11/18 at 11:31
 */
public class GenreClient {
    private static IGenreClient mIGenreClient =
            RetrofitClient.getClient().create(IGenreClient.class);

    @Nullable
    public static HashMap<Integer, String> getAllGenres() throws IOException {
        Call<GenreResponse> call = mIGenreClient.getGenres(Constants.GET_ALL_MANGAS_COUNTRY_CODE);

        Response<GenreResponse> response = call.execute();

        if (response.isSuccessful()
                && Objects.nonNull(response.body())
                && Objects.nonNull(response.body().getData())) {
            for (GenreResponse.ResponseData responseData : response.body().getData()) {
                if (responseData.getMsid() == Constants.GET_ALL_MANGAS_MSID_CODE) {
                    return responseData.getMangaGenres();
                }
            }
        }
        return null;
    }
}
