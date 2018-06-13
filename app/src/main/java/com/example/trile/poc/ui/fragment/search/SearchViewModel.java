package com.example.trile.poc.ui.fragment.search;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.database.model.Genre;
import com.example.trile.poc.repository.DataRepository;
import com.example.trile.poc.utils.InjectorUtils;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author trile
 * @since 6/11/18 at 12:35
 */
public class SearchViewModel extends AndroidViewModel {

    private final CompositeDisposable mCompositeDisposable;
    private final DataRepository mRepository;
    private final MutableLiveData<List<? extends Genre>> mMangaGenres;
    private final MutableLiveData<PagedList<MangaItemEntity>> mMangaResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        mCompositeDisposable = new CompositeDisposable();

        mRepository = InjectorUtils.provideRepository(application.getApplicationContext());

        mMangaGenres = new MutableLiveData<>();
        mCompositeDisposable.add(
                mRepository.getAllGenres()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(mMangaGenres::postValue)
        );

        mMangaResults = new MutableLiveData<>();
    }

    public LiveData<List<? extends Genre>> getMangaGenres() {
        return mMangaGenres;
    }

    public LiveData<PagedList<MangaItemEntity>> getMangaResults() {
        return mMangaResults;
    }

    public void filterManga(List<Integer> includeGenres,
                            List<Integer> excludeGenres,
                            String orderBy) {
        mCompositeDisposable.add(
                mRepository.filterManga
                        (
                                getApplication().getApplicationContext(),
                                includeGenres,
                                excludeGenres,
                                orderBy
                        )
                        .observeOn(Schedulers.io())
                        .subscribe(mMangaResults::postValue)
        );
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
            return (T) new SearchViewModel(mApplication);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }
}
