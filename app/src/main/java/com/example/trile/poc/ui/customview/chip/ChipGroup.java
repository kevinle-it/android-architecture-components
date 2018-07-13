package com.example.trile.poc.ui.customview.chip;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.trile.poc.R;
import com.example.trile.poc.utils.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author trile
 * @since 6/7/18 at 10:17
 */
public class ChipGroup extends RelativeLayout {

    private List<Chip> mChips;

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
        setUpListChips();
    }

    private void setUpListChips() {
        mChips = new ArrayList<>();
    }

    public List<Chip> getChips() {
        return mChips;
    }

    @Override
    public void addView(View child) {
        if (Objects.nonNull(child) && child instanceof Chip) {
            mChips.add((Chip) child);
        }
        super.addView(child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int childMargins = getResources().getDimensionPixelSize(R.dimen.chip_margin);

        final int containerLeftBoundForChildViews = this.getPaddingStart();

        // containerRightBoundForChildViews = containerWidth - paddingRight;
        final int containerRightBoundForChildViews =
                MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingEnd();

        int maxChildHeightOnCurrentLine = 0;

        final int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mCurrentLeftPosition = containerLeftBoundForChildViews;
        int rowCount = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(
                    maxWidth,
                    childMargins + child.getMeasuredWidth() + childMargins
            );

            if (childMargins + mCurrentLeftPosition + child.getMeasuredWidth() + childMargins >
                    containerRightBoundForChildViews - 200) {
                mCurrentLeftPosition = containerLeftBoundForChildViews;
                maxHeight += childMargins + maxChildHeightOnCurrentLine + childMargins;
                // Reset maxChildHeight for next line.
                maxChildHeightOnCurrentLine = 0;
            } else {
                maxHeight = Math.max(
                        maxHeight,
                        childMargins + child.getMeasuredHeight() + childMargins
                );
            }
            if (maxChildHeightOnCurrentLine < child.getMeasuredHeight()) {
                maxChildHeightOnCurrentLine = child.getMeasuredHeight();
            }
            mCurrentLeftPosition += childMargins + child.getMeasuredWidth() + childMargins;

            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int childMargins = getResources().getDimensionPixelSize(R.dimen.chip_margin);

        final int containerLeftBoundForChildViews = this.getPaddingStart();
        final int containerTopBoundForChildViews = this.getPaddingTop();

        // containerRightBoundForChildViews = containerWidth - paddingRight;
        final int containerRightBoundForChildViews =
                this.getMeasuredWidth() - this.getPaddingEnd();

        // containerBottomBoundForChildViews = containerHeight - paddingBottom;
        final int containerBottomBoundForChildViews =
                this.getMeasuredHeight() - this.getPaddingBottom();

        final int maxChildWidthInContainer =
                containerRightBoundForChildViews - containerLeftBoundForChildViews;
        final int maxChildHeightInContainer =
                containerBottomBoundForChildViews - containerTopBoundForChildViews;

        // containerLeftBoundForChildViews - childMargins: Don't add left margin on first Chip in a row.
        int currentLeftPosition = containerLeftBoundForChildViews - childMargins;
        int currentTopPosition = containerTopBoundForChildViews;
        int maxChildHeightOnCurrentLine = 0;

        int currentChildWidth;
        int currentChildHeight;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                return;
            }
            // Let the child to measure itself.
            //
            // MeasureSpec.makeMeasureSpec(maxChildWidthInContainer, MeasureSpec.AT_MOST) just
            // means combining the maxChildWidthInContainer & MeasureSpec.AT_MOST into an integer.
            //
            // The widthMeasureSpec & heightMeasureSpec created by below
            // MeasureSpec.makeMeasureSpec() will be pass to onMeasure() method in the Chip
            // Custom View after we call child.measure().
            //
            // The 2 measureSpecs will then be used by the child in its onMeasure() to set the
            // actual
            // width and height for itself through setMeasuredDimension().
            //
            // For example, the following code will be run in onMeasure() of the child after we
            // call child.measure():
            //
            // int desiredWidth = 100;  // Our actual desiredWidth.
            // int desiredHeight = 100; // Our actual desiredHeight.
            //
            // // Get the mode we created along with the size by above MeasureSpec.makeMeasureSpec()
            // int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            // int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            // int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            // int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            //
            // int width;
            // int height;
            //
            // // Measure Width
            // if (widthMode == MeasureSpec.EXACTLY) {
            //     // Must be this size
            //     width = widthSize;
            // } else if (widthMode == MeasureSpec.AT_MOST) {
            //     // Can't be bigger than...
            //     width = Math.min(desiredWidth, widthSize);   // if the widthSize (passed from
            //                                                  // parent) smaller than our desired
            //                                                  // width, then the child width must
            //                                                  // be widthSize at most.
            // } else { // MeasureSpec.UNSPECIFIED
            //     // Be whatever you want
            //     width = desiredWidth;
            // }
            //
            // // Measure Height
            // if (heightMode == MeasureSpec.EXACTLY) {
            //     // Must be this size
            //     height = heightSize;
            // } else if (heightMode == MeasureSpec.AT_MOST) {
            //     // Can't be bigger than...
            //     height = Math.min(desiredHeight, heightSize);    // if the heightSize (passed
            //                                                      // from parent) smaller than
            //                                                      // our desired width, then
            //                                                      // the child height must be
            //                                                      // heightSize at most.
            // } else { // MeasureSpec.UNSPECIFIED
            //     // Be whatever you want
            //     height = desiredHeight;
            // }
            //
            // // MUST CALL THIS
            // setMeasuredDimension(width, height);
            child.measure(
                    MeasureSpec.makeMeasureSpec(
                            maxChildWidthInContainer, MeasureSpec.AT_MOST
                    ),
                    MeasureSpec.makeMeasureSpec(
                            maxChildHeightInContainer, MeasureSpec.AT_MOST
                    )
            );
            currentChildWidth = child.getMeasuredWidth();
            currentChildHeight = child.getMeasuredHeight();

            // Exceeding Right Bound of Container (including leftChildMargin & rightChildMargin)
            // => Go to next line.
            if (childMargins + currentLeftPosition + currentChildWidth + childMargins >
                    containerRightBoundForChildViews - 200) {
                // containerLeftBoundForChildViews - childMargins: Don't add left margin on first Chip in a row.
                currentLeftPosition = containerLeftBoundForChildViews - childMargins;
                currentTopPosition += childMargins + maxChildHeightOnCurrentLine + childMargins;
                // Reset maxChildHeight for next line.
                maxChildHeightOnCurrentLine = 0;
            }

            child.layout(
                    childMargins + currentLeftPosition, // include leftMargin.
                    childMargins + currentTopPosition,  // include topMargin.

                    // include leftMargin & rightMargin.
                    childMargins + currentLeftPosition + currentChildWidth + childMargins,

                    // include topMargin & bottomMargin.
                    childMargins + currentTopPosition + currentChildHeight + childMargins
            );

            if (maxChildHeightOnCurrentLine < currentChildHeight) {
                maxChildHeightOnCurrentLine = currentChildHeight;
            }
            currentLeftPosition += childMargins + currentChildWidth + childMargins;
        }
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
