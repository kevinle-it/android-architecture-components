package com.example.trile.poc.helper;

import android.content.Context;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.repository.DataRepository;

public class InjectorUtils {
    public static DataRepository provideRepository(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getInstance(context, executors);
        MangaNetworkDataSource networkDataSource = provideNetworkDataSource(context);
        return DataRepository.getInstance(database, networkDataSource, executors);
    }

    public static MangaNetworkDataSource provideNetworkDataSource(Context context) {
        return MangaNetworkDataSource.getInstance(context.getApplicationContext());
    }
}
