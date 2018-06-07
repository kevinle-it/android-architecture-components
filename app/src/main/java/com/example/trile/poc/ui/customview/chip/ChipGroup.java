package com.example.trile.poc.ui.customview.chip;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.trile.poc.ui.helper.ViewUtils;
import com.example.trile.poc.utils.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author trile
 * @since 6/7/18 at 10:17
 */
public class ChipGroup extends RelativeLayout {

    private List<Chip> mGenres;

    public ChipGroup(Context context) {
        super(context);
        setUpControl();
    }

    public ChipGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpControl();
    }

    public ChipGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpControl();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChipGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setUpControl();
    }

    private void setUpControl() {
        setUpListGenres();
    }

    private void setUpListGenres() {
        mGenres = new ArrayList<>();
    }

    @Override
    public void addView(View child) {
        if (Objects.nonNull(child) && child instanceof Chip) {
            if (child.getId() == View.NO_ID) {
                child.setId(ViewUtils.generateViewId());
            }
            mGenres.add((Chip) child);
        }
        super.addView(child);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int chipGroupWidth = getWidth();

        int totalChipWidthOnCurrentLine = 0;
        boolean mIsGoToNextRow = false;
        int beginRowChipId = View.NO_ID;
        for (int i = 0; i < mGenres.size(); ++i) {
            if (mGenres.get(i).getVisibility() != GONE) {
                totalChipWidthOnCurrentLine += mGenres.get(i).getWidth();
                if (mIsGoToNextRow) {
                    beginRowChipId = mGenres.get(i).getId();

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    // TODO: 6/7/18 Fix the wrong rule (only the first child is positioned Start|Top
                    // TODO: in parent).
                    lp.addRule(RelativeLayout.ALIGN_PARENT_START);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mGenres.get(i).setLayoutParams(lp);

                    mIsGoToNextRow = false;

                    continue;
                }

                if (chipGroupWidth - totalChipWidthOnCurrentLine > 0) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    // TODO: 6/7/18 Fix the wrong rule (only the first child is positioned Start|Top
                    // TODO: in parent).
                    lp.addRule(RelativeLayout.ALIGN_PARENT_START);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mGenres.get(i).setLayoutParams(lp);
                } else {
                    mIsGoToNextRow = true;
                }
            }
        }

        // Must call super.onLayout() because it will position the childs based on the above
        // LayoutParams set to them.
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private static class LayoutParams extends RelativeLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }

        /**
         * <p>Fixes the child's width to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and the child's
         * height to  {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
         * when not specified in the XML file.</p>
         *
         * @param a          the styled attributes set
         * @param widthAttr  the width attribute to fetch
         * @param heightAttr the height attribute to fetch
         */
        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    private static class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {

        @Override
        public void onChildViewAdded(View parent, View child) {

        }

        @Override
        public void onChildViewRemoved(View parent, View child) {

        }
    }
}
