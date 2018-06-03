package com.example.trile.poc.repository.paging;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.content.Context;

import com.example.trile.poc.database.entity.MangaItemEntity;

public class MangaItemDataSourceFactory extends DataSource.Factory<Integer, MangaItemEntity> {

    private Context mContext;

    private String mOrderResultListBy;

    private MutableLiveData<MangaItemDataSource> mMangaItemDataSource = new MutableLiveData<>();

    public MangaItemDataSourceFactory(Context mContext, String mOrderResultListBy) {
        this.mContext = mContext;
        this.mOrderResultListBy = mOrderResultListBy;
    }

    /**
     * Create a DataSource.
     * <p>
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     * <p>
     * {@link LivePagedListBuilder} for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * {@code LiveData<PagedList>} to observers.
     *
     * @return the new DataSource.
     */
    @Override
    public DataSource<Integer, MangaItemEntity> create() {
        MangaItemDataSource mangaItemDataSource = new MangaItemDataSource(mContext, mOrderResultListBy);
        mMangaItemDataSource.postValue(mangaItemDataSource);
        return mangaItemDataSource;
    }

    public LiveData<MangaItemDataSource> getMangaItemDataSource() {
        return mMangaItemDataSource;
    }
}
