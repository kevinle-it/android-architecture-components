package com.example.trile.poc.api.service;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides the singleton of Retrofit Client.
 *
 * @author trile
 * @since 5/22/18 at 14:06
 */
public class RetrofitClient {

    public static final String TAG = RetrofitClient.class.getSimpleName();

    private static Retrofit mRetrofit = null;

    public static Retrofit getClient() {
        if (mRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofit == null) {
                    OkHttpClient httpClient =
                            new OkHttpClient.Builder()
                                    .addInterceptor(chain -> {
                                        Request original = chain.request();

                                        return chain.proceed(original);
                                    })
                                    .addInterceptor(
                                            new HttpLoggingInterceptor()
                                                    .setLevel(HttpLoggingInterceptor.Level.HEADERS)
                                    )
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .writeTimeout(8, TimeUnit.SECONDS)
                                    .readTimeout(5, TimeUnit.SECONDS)
                                    .build();

                    Gson gson = new GsonBuilder()
                            .serializeNulls()
//                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                            .create();

                    mRetrofit = new Retrofit.Builder()
                            .client(httpClient)
                            .baseUrl(Constants.MANGA_ROCK_SERVER_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .callbackExecutor(AppExecutors.getInstance().networkIO())
                            .build();
                }
            }
        }
        return mRetrofit;
    }
}
