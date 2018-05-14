package com.example.trile.poc.api;

import com.example.trile.poc.helper.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String TAG = RetrofitClient.class.getSimpleName();

    private static Retrofit mRetrofit = null;

    public static Retrofit getClient() {
        if (mRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (mRetrofit == null) {
                    OkHttpClient httpClient =
                            new OkHttpClient.Builder()
                                    .addInterceptor(new Interceptor() {
                                        @Override
                                        public Response intercept(Chain chain) throws IOException {
                                            Request original = chain.request();

                                            return chain.proceed(original);
                                        }
                                    })
                                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .writeTimeout(8, TimeUnit.SECONDS)
                                    .readTimeout(5, TimeUnit.SECONDS)
                                    .build();

                    Gson gson = new GsonBuilder()
                            .serializeNulls()
//                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                            .create();

                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(Constants.MANGA_ROCK_SERVER_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(httpClient)
                            .build();
                }
            }
        }
        return mRetrofit;
    }
}
