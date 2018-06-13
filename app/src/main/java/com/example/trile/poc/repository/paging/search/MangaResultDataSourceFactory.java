package com.example.trile.poc.repository.paging.search;

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
public class MangaResultDataSourceFactory extends DataSource.Factory<Integer, MangaItemEntity> {

    private Context mContext;

    private List<Integer> mIncludeGenres;
    private List<Integer> mExcludeGenres;

    private String mOrderResultListBy;

    private MutableLiveData<MangaResultDataSource> mMangaResultDataSource;

    public MangaResultDataSourceFactory(Context context,
                                        List<Integer> includeGenres,
                                        List<Integer> excludeGenres,
                                        String orderResultListBy) {
        mContext = context;
        mIncludeGenres = includeGenres;
        mExcludeGenres = excludeGenres;
        mOrderResultListBy = orderResultListBy;
        mMangaResultDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, MangaItemEntity> create() {
        MangaResultDataSource mangaResultDataSource =
                new MangaResultDataSource(
                        mContext,
                        mIncludeGenres,
                        mExcludeGenres,
                        mOrderResultListBy
                );
        mMangaResultDataSource.postValue(mangaResultDataSource);
        return mangaResultDataSource;
    }

    public LiveData<MangaResultDataSource> getMangaResultDataSource() {
        return mMangaResultDataSource;
    }
}
