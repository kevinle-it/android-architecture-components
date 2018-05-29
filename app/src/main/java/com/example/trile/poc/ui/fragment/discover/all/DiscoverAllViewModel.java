package com.example.trile.poc.ui.fragment.discover.all;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.repository.DataRepository;

/**
 * {@link ViewModel} for {@link DiscoverAllFragment} to retain data
 * (the Repository & List of Manga Items) on configuration changes.
 *
 * @author trile
 * @since 5/22/18 at 14:11
 */
public class DiscoverAllViewModel extends ViewModel {

    private final DataRepository mRepository;
    private final MutableLiveData<String> mOrderBy;
    private final LiveData<PagedList<MangaItemEntity>> mMangaItems;

    public DiscoverAllViewModel(DataRepository repository) {
        mRepository = repository;
        mOrderBy = new MutableLiveData<>();
        mMangaItems = Transformations.switchMap(mOrderBy, mRepository::getAllMangaItems);
    }

    public LiveData<PagedList<MangaItemEntity>> getAllMangaItems() {
        return mMangaItems;
    }

    public LiveData<String> getOrderBy() {
        return mOrderBy;
    }

    public void setOrderBy(String orderBy) {
        mOrderBy.postValue(orderBy);
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
