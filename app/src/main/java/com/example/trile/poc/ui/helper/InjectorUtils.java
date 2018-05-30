package com.example.trile.poc.ui.helper;

import android.content.Context;

import com.example.trile.poc.AppExecutors;
import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.repository.DataRepository;
import com.example.trile.poc.ui.fragment.discover.all.DiscoverAllViewModel;

/**
 * Provides static methods to inject the various classes needed for Manga Rock.
 *
 * @author trile
 * @since 5/22/18 at 14:16
 */
public class InjectorUtils {
    public static DataRepository provideRepository(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext(), executors);
        MangaNetworkDataSource networkDataSource = provideNetworkDataSource();
        return DataRepository.getInstance(database, networkDataSource, executors);
    }

    public static MangaNetworkDataSource provideNetworkDataSource() {
        return MangaNetworkDataSource.getInstance();
    }

    public static DiscoverAllViewModel.Factory provideDiscoverAllViewModelFactory(Context context) {
        DataRepository repository = provideRepository(context.getApplicationContext());
        return new DiscoverAllViewModel.Factory(repository);
    }
}
