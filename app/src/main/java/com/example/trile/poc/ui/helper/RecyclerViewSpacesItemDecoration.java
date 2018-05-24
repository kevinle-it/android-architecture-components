package com.example.trile.poc.ui.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author trile
 * @since 5/24/18 at 11:52
 */
public class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpacingInPixels;

    public RecyclerViewSpacesItemDecoration(int spacingInPixels) {
        this.mSpacingInPixels = spacingInPixels;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpacingInPixels;
        outRect.top = mSpacingInPixels;
        outRect.right = mSpacingInPixels;
        outRect.bottom = mSpacingInPixels;
    }
}
