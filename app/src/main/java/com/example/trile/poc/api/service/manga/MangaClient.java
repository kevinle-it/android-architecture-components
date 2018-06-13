package com.example.trile.poc.api.service.manga;

import android.support.annotation.Nullable;

import com.example.trile.poc.Constants;
import com.example.trile.poc.api.model.MangaResponse;
import com.example.trile.poc.api.service.RetrofitClient;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.utils.Objects;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Handle sending network requests.
 *
 * @author trile
 * @since 5/22/18 at 14:00
 */
public class MangaClient {
    public static final String TAG = MangaClient.class.getSimpleName();
    private static IMangaClient mIMangaClient =
            RetrofitClient.getClient().create(IMangaClient.class);

    @Nullable
    public static List<MangaItemEntity> getAllMangaItems() throws IOException {
        Call<MangaResponse> call =
                mIMangaClient.getAllMangaItems(
                        Constants.GET_ALL_MANGAS_MSID_CODE, Constants.GET_ALL_MANGAS_COUNTRY_CODE
                );

        Response<MangaResponse> response = call.execute();

        if (response.isSuccessful()
                && Objects.nonNull(response.body())
                && Objects.nonNull(response.body().getData())) {
            return response.body().getData().getMangaItems();
        }
        return null;
    }
}
