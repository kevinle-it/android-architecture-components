package com.example.trile.poc.repository.paging.discover.all;

import android.arch.paging.DataSource;
import android.arch.paging.PagedList;
import android.arch.paging.PositionalDataSource;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trile.poc.database.AppDatabase;
import com.example.trile.poc.database.entity.MangaItemEntity;
import com.example.trile.poc.utils.InjectorUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MangaItemDataSource extends PositionalDataSource<MangaItemEntity> {

    private AppDatabase mDatabase;

    private String mOrderResultListBy;

    @SuppressWarnings("FieldCanBeLocal")
    private final InvalidationTracker.Observer mObserver;

    public MangaItemDataSource(Context context, String orderResultListBy) {
        mDatabase = InjectorUtils.provideAppDatabase(context.getApplicationContext());
        mOrderResultListBy = orderResultListBy;

        /**
         * Using {@link InvalidationTracker.Observer(String firstTableName, String... restTableNames)}
         * to observe for changes in {@link MangaItemEntity} table in the {@link AppDatabase}
         * to invalidate the {@link MangaItemDataSource} for making a reload of data
         * from the {@link AppDatabase} to populate the {@link PagedList}.
         */
        mObserver = new InvalidationTracker.Observer(MangaItemEntity.class.getSimpleName()) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                invalidate();
            }
        };
        mDatabase.getInvalidationTracker().addObserver(mObserver);
    }

    /**
     * Load initial list data.
     * <p>
     * This method is called to load the initial page(s) from the DataSource.
     * <p>
     * Result list must be a multiple of pageSize to enable efficient tiling.
     *
     * @param params   Parameters for initial load, including requested start position, load size, and
     *                 page size.
     * @param callback Callback that receives initial load data, including.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<MangaItemEntity> callback) {
        int totalCount = countItems();
        if (totalCount == 0) {
            callback.onResult(Collections.emptyList(), 0, 0);
            return;
        }

        // bound the size requested, based on known count.
        final int firstLoadPosition = computeInitialLoadPosition(params, totalCount);
        final int firstLoadSize = computeInitialLoadSize(params, firstLoadPosition, totalCount);

        List<MangaItemEntity> list = loadRange(firstLoadPosition, firstLoadSize);
        if (list != null && list.size() == firstLoadSize) {
            callback.onResult(list, firstLoadPosition, totalCount);
        } else {
            // null list, or size doesn't match request - DB modified between count and load.
            invalidate();
        }
    }

    /**
     * Called to load a range of data from the DataSource.
     * <p>
     * This method is called to load additional pages from the DataSource after the
     * LoadInitialCallback passed to dispatchLoadInitial has initialized a PagedList.
     * <p>
     * Unlike {@link #loadInitial(LoadInitialParams, LoadInitialCallback)}, this method must return
     * the number of items requested, at the position requested.
     *
     * @param params   Parameters for load, including start position and load size.
     * @param callback Callback that receives loaded data.
     */
    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<MangaItemEntity> callback) {
        List<MangaItemEntity> list = loadRange(params.startPosition, params.loadSize);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (list != null) {
            callback.onResult(list);
        } else {
            invalidate();
        }
    }

    private int countItems() {
        /**
         * We should check whether the Database is Empty or Not before return the LIMIT constraint.
         *
         * Reason: always return 500 items even when the AppDatabase is Empty result in
         * the invoking of {@link DataSource#invalidate()} method in
         * {@link PositionalDataSource#loadInitial()} which leads to
         * the MangaItemDataSource to be recreated and {@link PositionalDataSource#loadInitial()}
         * is called again then {@link DataSource#invalidate()} is invoked and so on.
         *
         * This create an indeterminate loop that the MangaItemDataSource cannot ever pass the
         * Empty Result List to
         * {@link LoadInitialCallback#onResult(List Collections.emptyList, int 0, int 0)}
         * -- which can invoke the BoundaryCallback running to fetch Manga Items from Server
         * then insert them all into {@link AppDatabase}.
         */
        if (mDatabase.mangaItemDAO().countAllMangaItems() > 0) {// means we have thousands of items.
            return 500; // Only get first 500 Manga Items for a smooth fast scrolling.
        }
        return 0;
    }

    /**
     * Return the rows from startPos to startPos + loadCount.
     */
    @Nullable
    public List<MangaItemEntity> loadRange(int startPosition, int loadCount) {
        if (startPosition + loadCount <= 500) { // Only get first 500 Manga Items for a smooth fast scrolling.
            return mDatabase.mangaItemDAO()
                    .loadMangaItemsRange(startPosition, loadCount, mOrderResultListBy);
        }
        return null;
    }
}
