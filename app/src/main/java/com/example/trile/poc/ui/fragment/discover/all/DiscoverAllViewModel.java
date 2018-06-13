package com.example.trile.poc.ui.fragment.discover.all;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.repository.DataRepository;
import com.example.trile.poc.utils.InjectorUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link ViewModel} for {@link DiscoverAllFragment} to retain data
 * (the Repository & List of Manga Items) on configuration changes.
 *
 * @author trile
 * @since 5/22/18 at 14:11
 */
public class DiscoverAllViewModel extends AndroidViewModel {

    private final CompositeDisposable mCompositeDisposable;
    private final DataRepository mRepository;
    private final MutableLiveData<String> mOrderBy;
    private final MutableLiveData<PagedList<MangaItemEntity>> mMangaItems;
    private int mRecyclerViewScrollOffsetX;
    private int mRecyclerViewScrollOffsetY;

    public DiscoverAllViewModel(@NonNull Application application) {
        super(application);
        mCompositeDisposable = new CompositeDisposable();

        mRepository = InjectorUtils.provideRepository(application.getApplicationContext());

        mOrderBy = new MutableLiveData<>();
        mMangaItems = new MutableLiveData<>();
        mRecyclerViewScrollOffsetX = 0;
        mRecyclerViewScrollOffsetY = 0;
    }

    public LiveData<PagedList<MangaItemEntity>> getAllMangaItems() {
        return mMangaItems;
    }

    public LiveData<String> getOrderBy() {
        return mOrderBy;
    }

    public void setOrderBy(String orderBy) {
        mOrderBy.postValue(orderBy);

        mCompositeDisposable.add(
                mRepository.getAllMangaItems
                        (
                                getApplication().getApplicationContext(),
                                orderBy
                        )
                        .observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .subscribe(mMangaItems::postValue)
        );
    }

    public int getRecyclerViewScrollOffsetX() {
        return mRecyclerViewScrollOffsetX;
    }

    public int getRecyclerViewScrollOffsetY() {
        return mRecyclerViewScrollOffsetY;
    }

    public void setRecyclerViewScrollOffset(int recyclerViewScrollOffsetX,
                                            int recyclerViewScrollOffsetY) {
        mRecyclerViewScrollOffsetX = recyclerViewScrollOffsetX;
        mRecyclerViewScrollOffsetY = recyclerViewScrollOffsetY;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        public Factory(@NonNull Application application) {
            mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DiscoverAllViewModel(mApplication);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }
}
