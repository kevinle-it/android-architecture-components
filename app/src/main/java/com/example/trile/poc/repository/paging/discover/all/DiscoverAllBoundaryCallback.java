package com.example.trile.poc.repository.paging.discover.all;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.trile.poc.api.service.MangaNetworkDataSource;
import com.example.trile.poc.database.entity.MangaItemEntity;

/**
 * @author trile
 * @since 5/30/18 at 12:41
 */
public class DiscoverAllBoundaryCallback extends PagedList.BoundaryCallback<MangaItemEntity> {

    private final MangaNetworkDataSource mMangaNetworkDataSource;

    public DiscoverAllBoundaryCallback(MangaNetworkDataSource mangaNetworkDataSource) {
        mMangaNetworkDataSource = mangaNetworkDataSource;
    }

    @Override
    public void onZeroItemsLoaded() {
        mMangaNetworkDataSource.startFetchAllMangaItems();
    }

    @Override
    public void onItemAtEndLoaded(@NonNull MangaItemEntity itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);     // Method has not yet implemented because the
                                                // current version of Manga Rock Server returns
                                                // all Manga at once.
    }
}
