package com.example.trile.poc.repository.paging.search.genre;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.content.Context;

import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * @author trile
 * @since 6/12/18 at 16:20
 */
public class FilterByGenreResultDataSourceFactory extends DataSource.Factory<Integer, MangaItemEntity> {

    private Context mContext;

    private List<Integer> mIncludeGenres;
    private List<Integer> mExcludeGenres;

    private String mOrderResultListBy;

    private MutableLiveData<FilterByGenreResultDataSource> mFilterByGenreResultDataSource;

    public FilterByGenreResultDataSourceFactory(Context context,
                                                List<Integer> includeGenres,
                                                List<Integer> excludeGenres,
                                                String orderResultListBy) {
        mContext = context;
        mIncludeGenres = includeGenres;
        mExcludeGenres = excludeGenres;
        mOrderResultListBy = orderResultListBy;
        mFilterByGenreResultDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, MangaItemEntity> create() {
        FilterByGenreResultDataSource filterByGenreResultDataSource =
                new FilterByGenreResultDataSource(
                        mContext,
                        mIncludeGenres,
                        mExcludeGenres,
                        mOrderResultListBy
                );
        mFilterByGenreResultDataSource.postValue(filterByGenreResultDataSource);
        return filterByGenreResultDataSource;
    }

    public LiveData<FilterByGenreResultDataSource> getFilterByGenreResultDataSource() {
        return mFilterByGenreResultDataSource;
    }
}
