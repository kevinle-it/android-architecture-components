package com.example.trile.poc.repository.paging.search.name;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.content.Context;

import com.example.trile.poc.database.entity.MangaItemEntity;

import java.util.List;

/**
 * @author trile
 * @since 6/13/18 at 16:51
 */
public class FilterByNameResultDataSourceFactory extends DataSource.Factory<Integer, MangaItemEntity> {

    private Context mContext;

    private String mMangaName;
    private String mOrderResultListBy;

    private MutableLiveData<FilterByNameResultDataSource> mFilterByNameResultDataSource;

    public FilterByNameResultDataSourceFactory(Context context,
                                               String mangaName,
                                               String orderResultListBy) {
        mContext = context;
        mMangaName = mangaName;
        mOrderResultListBy = orderResultListBy;
        mFilterByNameResultDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, MangaItemEntity> create() {
        FilterByNameResultDataSource filterByNameResultDataSource =
                new FilterByNameResultDataSource(
                        mContext,
                        mMangaName,
                        mOrderResultListBy
                );
        mFilterByNameResultDataSource.postValue(filterByNameResultDataSource);
        return filterByNameResultDataSource;
    }

    public LiveData<FilterByNameResultDataSource> getFilterByNameResultDataSource() {
        return mFilterByNameResultDataSource;
    }
}
