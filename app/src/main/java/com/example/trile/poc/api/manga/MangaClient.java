package com.example.trile.poc.api.manga;

import com.example.trile.poc.api.RetrofitClient;
import com.example.trile.poc.api.manga.get.IGetAllMangasCallback;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.helper.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaClient {
    public static final String TAG = MangaClient.class.getSimpleName();
    private static IMangaClient mIMangaClient = RetrofitClient.getClient().create(IMangaClient.class);

    public static void getAllMangas(final IGetAllMangasCallback getAllMangasCallback) {
        Call<List<MangaItemEntity>> call = mIMangaClient.getAllMangas(Constants.GET_ALL_MANGAS_MSID_CODE, Constants.GET_ALL_MANGAS_COUNTRY_CODE);
        Callback<List<MangaItemEntity>> callback = new Callback<List<MangaItemEntity>>() {
            @Override
            public void onResponse(Call<List<MangaItemEntity>> call, Response<List<MangaItemEntity>> response) {
                getAllMangasCallback.onGetComplete(response.body());
            }

            @Override
            public void onFailure(Call<List<MangaItemEntity>> call, Throwable t) {
                getAllMangasCallback.onGetFailure(t);
            }
        };
        call.enqueue(callback);
    }
}
