package com.example.trile.poc.repository.paging.search.name;

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

/**
 * @author trile
 * @since 6/13/18 at 16:04
 */
public class FilterByNameResultDataSource extends PositionalDataSource<MangaItemEntity> {

    private AppDatabase mDatabase;

    private String mMangaName;
    private String mOrderResultListBy;

    @SuppressWarnings("FieldCanBeLocal")
    private final InvalidationTracker.Observer mObserver;

    public FilterByNameResultDataSource(Context context,
                                        String mangaName,
                                        String orderResultListBy) {
        mDatabase = InjectorUtils.provideAppDatabase(context.getApplicationContext());
        mMangaName = mangaName;
        mOrderResultListBy = orderResultListBy;

        mObserver = new InvalidationTracker.Observer(MangaItemEntity.class.getSimpleName()) {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
                invalidate();
            }
        };
        mDatabase.getInvalidationTracker().addObserver(mObserver);
    }

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

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<MangaItemEntity> callback) {
        List<MangaItemEntity> list = loadRange(params.startPosition, params.loadSize);
        if (list != null) {
            callback.onResult(list);
        } else {
            invalidate();
        }
    }

    private int countItems() {
        return mDatabase.mangaItemDAO()
                .countFilteredMangaByName(mMangaName);
    }

    /**
     * Return the rows from startPos to startPos + loadCount.
     */
    @Nullable
    public List<MangaItemEntity> loadRange(int startPosition, int loadCount) {
        if (startPosition + loadCount <= 500) { // Only get first 500 Manga Items for a smooth fast scrolling.
            return mDatabase.mangaItemDAO()
                    .filterMangaByName(
                            startPosition,
                            loadCount,
                            mMangaName,
                            mOrderResultListBy
                    );
        }
        return null;
    }
}
