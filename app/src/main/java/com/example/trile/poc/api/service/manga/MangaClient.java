package com.example.trile.poc.api.service.manga;

import android.arch.lifecycle.MutableLiveData;

import com.example.trile.poc.Constants;
import com.example.trile.poc.api.model.MangaResponse;
import com.example.trile.poc.api.service.RetrofitClient;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.utils.Objects;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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

    public static void getAllMangaItems(final MutableLiveData<List<MangaItemEntity>> downloadedMangaItems) {
        Call<MangaResponse> call =
                mIMangaClient.getAllMangaItems(
                        Constants.GET_ALL_MANGAS_MSID_CODE, Constants.GET_ALL_MANGAS_COUNTRY_CODE
                );
        Callback<MangaResponse> callback = new Callback<MangaResponse>() {
            @Override
            public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                if (Objects.nonNull(response.body())
                        && Objects.nonNull(response.body().getData())) {
                    downloadedMangaItems.postValue(response.body().getData().getMangaItems());
                } else {
                    downloadedMangaItems.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MangaResponse> call, Throwable t) {

            }
        };
        call.enqueue(callback);
    }
}
