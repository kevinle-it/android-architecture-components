package com.example.trile.poc.ui.fragment.discover.all;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.repository.DataRepository;

import java.util.List;

public class DiscoverAllViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final LiveData<List<MangaItemEntity>> mMangaItems;

    public DiscoverAllViewModel(DataRepository repository) {
        mRepository = repository;
        mMangaItems = mRepository.getAllMangaItems();
    }

    public LiveData<List<MangaItemEntity>> getAllMangaItems() {
        return mMangaItems;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final DataRepository mRepository;

        public Factory(DataRepository repository) {
            mRepository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DiscoverAllViewModel(mRepository);
        }
    }
}
