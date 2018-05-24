package com.example.trile.poc.ui.helper;

import android.content.Context;
import android.support.v4.util.Pair;

import com.example.trile.poc.R;

/**
 * @author trile
 * @since 5/24/18 at 10:55
 */
public class RecyclerGridViewHelper {
    public static Pair<Integer, Integer> calculateFitScreenSpanCount(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int recyclerViewPadding = context.getResources().getDimensionPixelSize(R.dimen
                .recycler_grid_view_padding);

        // Actual Recycler View Width = screenWidth - (leftPadding + rightPadding).
        int recyclerViewWidth = screenWidth - (2 * recyclerViewPadding);
        // Min Manga Item Width = 109dp => convert to Pixels.
        int minMangaItemWidth = context.getResources().getDimensionPixelSize(R.dimen
                .manga_item_min_width);
        // 4 sides' paddings of each manga item = 4dp => convert to Pixels.
        int mangaItemSpacing = context.getResources().getDimensionPixelSize(R.dimen
                .recycler_grid_view_item_spacing);

        int columnCount = 1;
        // currentMangaItemWidth = actualRecyclerViewWidth - (leftItemSpace + rightItemSpace)
        int currentMangaItemWidth = recyclerViewWidth - (2 * mangaItemSpacing);
        int tempCurrentMangaItemWidth = currentMangaItemWidth;

        while (tempCurrentMangaItemWidth > minMangaItemWidth) {
            ++columnCount;
            // MangaItemWidth =
            // (actualRecyclerViewWidth - ((leftItemSpace + rightItemSpace) * columnCount)) / columnCount
            tempCurrentMangaItemWidth = (recyclerViewWidth - (2 * mangaItemSpacing * columnCount)) / columnCount;
            if (tempCurrentMangaItemWidth >= minMangaItemWidth) {
                currentMangaItemWidth = tempCurrentMangaItemWidth;
            }
        }

        // mangaItemHeight = 3/2 of mangaItemWidth + mangaInfoHeight;
        // 3/2 of mangaItemWidth = Manga Cover Image Height => MangaCover ratio = 2:3
        int mangaItemImageHeight = Math.round(1.5f * currentMangaItemWidth);
        if (tempCurrentMangaItemWidth < minMangaItemWidth) {
            return new Pair<>(--columnCount, mangaItemImageHeight);
        }
        return new Pair(columnCount, mangaItemImageHeight);
    }
}
