package com.example.trile.poc.utils;

import android.app.Application;
import android.content.Context;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.repository.DataRepository;
import com.example.trile.poc.ui.fragment.discover.all.DiscoverAllViewModel;
import com.example.trile.poc.ui.fragment.search.SearchViewModel;

/**
 * Provides static methods to inject the various classes needed for Manga Rock.
 *
 * @author trile
 * @since 5/22/18 at 14:16
 */
public class InjectorUtils {
    public static AppExecutors provideAppExecutors() {
        return AppExecutors.getInstance();
    }

    public static MangaNetworkDataSource provideNetworkDataSource() {
        return MangaNetworkDataSource.getInstance();
    }

    public static AppDatabase provideAppDatabase(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return AppDatabase.getInstance(context.getApplicationContext(), executors);
    }

    public static DataRepository provideRepository(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext(), executors);
        MangaNetworkDataSource networkDataSource = provideNetworkDataSource();
        return DataRepository.getInstance(database, networkDataSource, executors);
    }

    public static DiscoverAllViewModel.Factory provideDiscoverAllViewModelFactory(Application application) {
        return new DiscoverAllViewModel.Factory(application);
    }

    public static SearchViewModel.Factory provideSearchViewModelFactory(Application application) {
        return new SearchViewModel.Factory(application);
    }
}
