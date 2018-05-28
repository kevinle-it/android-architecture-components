package com.example.trile.poc.ui.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.trile.poc.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Customize the {@link android.support.v7.widget.FastScroller}
 * to show fast scroller for {@link RecyclerView}.
 * @author trile
 * @since 5/28/18 at 16:49
 */
public class CustomFastScroller extends RecyclerView.ItemDecoration
        implements RecyclerView.OnItemTouchListener {
    @IntDef({STATE_HIDDEN, STATE_VISIBLE, STATE_DRAGGING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State { }
    // Scroll thumb not showing
    private static final int STATE_HIDDEN = 0;
    // Scroll thumb visible and moving along with the scrollbar
    private static final int STATE_VISIBLE = 1;
    // Scroll thumb being dragged by user
    private static final int STATE_DRAGGING = 2;

    @IntDef({DRAG_X, DRAG_Y, DRAG_NONE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface DragState{ }
    private static final int DRAG_NONE = 0;
    private static final int DRAG_X = 1;
    private static final int DRAG_Y = 2;

    @IntDef({ANIMATION_STATE_OUT, ANIMATION_STATE_FADING_IN, ANIMATION_STATE_IN,
            ANIMATION_STATE_FADING_OUT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface AnimationState { }
    private static final int ANIMATION_STATE_OUT = 0;
    private static final int ANIMATION_STATE_FADING_IN = 1;
    private static final int ANIMATION_STATE_IN = 2;
    private static final int ANIMATION_STATE_FADING_OUT = 3;

    private static final int SHOW_DURATION_MS = 500;
    private static final int HIDE_DELAY_AFTER_VISIBLE_MS = 1500;
    private static final int HIDE_DELAY_AFTER_DRAGGING_MS = 1200;
    private static final int HIDE_DURATION_MS = 500;
    private static final int SCROLLBAR_FULL_OPAQUE = 255;

    private static final int[] PRESSED_STATE_SET = new int[]{android.R.attr.state_pressed};
    private static final int[] EMPTY_STATE_SET = new int[]{};

    private final int mScrollbarMinimumRange;
    private final int mMargin;

    // Final values for the vertical scroll bar
    private final StateListDrawable mVerticalThumbDrawable;
    private final Drawable mVerticalTrackDrawable;
    private final int mVerticalThumbWidth;
    private final int mVerticalTrackWidth;
    private final Paint mVerticalThumbTextPaint;
    private final int mVerticalThumbTextWidth;
    private final int mVerticalThumbTextHeight;
    private int mThumbTextItemPosition;

    // Final values for the horizontal scroll bar
    private final StateListDrawable mHorizontalThumbDrawable;
    private final Drawable mHorizontalTrackDrawable;
    private final int mHorizontalThumbHeight;
    private final int mHorizontalTrackHeight;

    // Dynamic values for the vertical scroll bar
    private int mVerticalThumbHeight;
    private int mVerticalThumbCenterY;
    private float mVerticalDragY;

    // Dynamic values for the horizontal scroll bar
    private int mHorizontalThumbWidth;
    private int mHorizontalThumbCenterX;
    private float mHorizontalDragX;

    private int mRecyclerViewWidth = 0;
    private int mRecyclerViewHeight = 0;

    private Context mContext;
    private RecyclerView mRecyclerView;
    /**
     * Whether the document is long/wide enough to require scrolling. If not, we don't show the
     * relevant scroller.
     */
    private boolean mNeedVerticalScrollbar = false;
    private boolean mNeedHorizontalScrollbar = false;
    @CustomFastScroller.State
    private int mState = STATE_HIDDEN;
    @CustomFastScroller.DragState
    private int mDragState = DRAG_NONE;

    private final int[] mVerticalRange = new int[2];
    private final int[] mHorizontalRange = new int[2];
    private final ValueAnimator mShowHideAnimator = ValueAnimator.ofFloat(0, 1);
    @CustomFastScroller.AnimationState
    private int mAnimationState = ANIMATION_STATE_OUT;
    private final Runnable mHideRunnable = () -> hide(HIDE_DURATION_MS);
    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            updateScrollPosition(recyclerView.computeHorizontalScrollOffset(),
                    recyclerView.computeVerticalScrollOffset());
        }
    };

    public CustomFastScroller(Context context, RecyclerView recyclerView,
                              StateListDrawable verticalThumbDrawable, Drawable verticalTrackDrawable,
                              StateListDrawable horizontalThumbDrawable, Drawable horizontalTrackDrawable,
                              int defaultWidth, int scrollbarMinimumRange, int margin) {
        mContext = context;
        mVerticalThumbDrawable = verticalThumbDrawable;
        mVerticalTrackDrawable = verticalTrackDrawable;
        mHorizontalThumbDrawable = horizontalThumbDrawable;
        mHorizontalTrackDrawable = horizontalTrackDrawable;
        mVerticalThumbWidth = Math.max(defaultWidth, verticalThumbDrawable.getIntrinsicWidth());
        mVerticalThumbHeight = mContext.getResources().getDimensionPixelOffset(R.dimen
                .recycler_grid_view_fast_scroller_thumb_height);
        mVerticalTrackWidth = Math.max(defaultWidth, verticalTrackDrawable.getIntrinsicWidth());

        mVerticalThumbTextWidth = mContext.getResources().getDimensionPixelOffset(R.dimen
                .recycler_grid_view_fast_scroller_thumb_text_width);
        mVerticalThumbTextHeight = mContext.getResources().getDimensionPixelOffset(R.dimen
                .recycler_grid_view_fast_scroller_thumb_text_height);

        mVerticalThumbTextPaint = new Paint();
        mVerticalThumbTextPaint.setColor(Color.BLACK);
        mVerticalThumbTextPaint.setTextSize(12 * mContext.getResources().getDisplayMetrics().density);
        mVerticalThumbTextPaint.setAlpha(SCROLLBAR_FULL_OPAQUE);

        mThumbTextItemPosition = 1;

        mHorizontalThumbHeight = Math
                .max(defaultWidth, horizontalThumbDrawable.getIntrinsicWidth());
        mHorizontalTrackHeight = Math
                .max(defaultWidth, horizontalTrackDrawable.getIntrinsicWidth());
        mScrollbarMinimumRange = scrollbarMinimumRange;
        mMargin = margin;
        mVerticalThumbDrawable.setAlpha(SCROLLBAR_FULL_OPAQUE);
        mVerticalTrackDrawable.setAlpha(SCROLLBAR_FULL_OPAQUE);

        mShowHideAnimator.addListener(new AnimatorListener());
        mShowHideAnimator.addUpdateListener(new AnimatorUpdater());

        attachToRecyclerView(recyclerView);
    }

    private void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return; // nothing to do
        }
        if (mRecyclerView != null) {
            destroyCallbacks();
        }
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            setupCallbacks();
        }
    }

    private void setupCallbacks() {
        mRecyclerView.addItemDecoration(this);
        mRecyclerView.addOnItemTouchListener(this);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void destroyCallbacks() {
        mRecyclerView.removeItemDecoration(this);
        mRecyclerView.removeOnItemTouchListener(this);
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        cancelHide();
    }

    private void requestRedraw() {
        mRecyclerView.invalidate();
    }

    private void setState(@CustomFastScroller.State int state) {
        if (state == STATE_DRAGGING && mState != STATE_DRAGGING) {
            mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
            cancelHide();
        }

        if (state == STATE_HIDDEN) {
            requestRedraw();
        } else {
            show();
        }

        if (mState == STATE_DRAGGING && state != STATE_DRAGGING) {
            mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
            resetHideDelay(HIDE_DELAY_AFTER_DRAGGING_MS);
        } else if (state == STATE_VISIBLE) {
            resetHideDelay(HIDE_DELAY_AFTER_VISIBLE_MS);
        }
        mState = state;
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(mRecyclerView) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }


    @SuppressLint("SwitchIntDef")
    private void show() {
        switch (mAnimationState) {
            case ANIMATION_STATE_FADING_OUT:
                mShowHideAnimator.cancel();
                // fall through
            case ANIMATION_STATE_OUT:
                mAnimationState = ANIMATION_STATE_FADING_IN;
                mShowHideAnimator.setFloatValues((float) mShowHideAnimator.getAnimatedValue(), 1);
                mShowHideAnimator.setDuration(SHOW_DURATION_MS);
                mShowHideAnimator.setStartDelay(0);
                mShowHideAnimator.start();
                break;
        }
    }


    @SuppressLint("SwitchIntDef")
    private void hide(int duration) {
        switch (mAnimationState) {
            case ANIMATION_STATE_FADING_IN:
                mShowHideAnimator.cancel();
                // fall through
            case ANIMATION_STATE_IN:
                mAnimationState = ANIMATION_STATE_FADING_OUT;
                mShowHideAnimator.setFloatValues((float) mShowHideAnimator.getAnimatedValue(), 0);
                mShowHideAnimator.setDuration(duration);
                mShowHideAnimator.start();
                break;
        }
    }

    private void cancelHide() {
        mRecyclerView.removeCallbacks(mHideRunnable);
    }

    private void resetHideDelay(int delay) {
        cancelHide();
        mRecyclerView.postDelayed(mHideRunnable, delay);
    }

    /**
     * Draw any appropriate decorations into the Canvas supplied to the RecyclerView.
     * Any content drawn by this method will be drawn after the item views are drawn
     * and will thus appear over the views.
     *
     * @param canvas Canvas to draw into
     * @param parent RecyclerView this ItemDecoration is drawing into
     * @param state  The current state of RecyclerView.
     */
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mRecyclerViewWidth != mRecyclerView.getWidth()
                || mRecyclerViewHeight != mRecyclerView.getHeight()) {
            mRecyclerViewWidth = mRecyclerView.getWidth();
            mRecyclerViewHeight = mRecyclerView.getHeight();
            // This is due to the different events ordering when keyboard is opened or
            // retracted vs rotate. Hence to avoid corner cases we just disable the
            // scroller when size changed, and wait until the scroll position is recomputed
            // before showing it back.
            setState(STATE_HIDDEN);
            return;
        }

        if (mAnimationState != ANIMATION_STATE_OUT) {
            if (mNeedVerticalScrollbar) {
                drawVerticalScrollbar(canvas);
            }
            if (mNeedHorizontalScrollbar) {
                drawHorizontalScrollbar(canvas);
            }
        }
    }

    private void drawVerticalScrollbar(Canvas canvas) {
        int viewWidth = mRecyclerViewWidth;

        int left = viewWidth - mVerticalThumbWidth;
        int top = mVerticalThumbCenterY - mVerticalThumbHeight / 2;
        mVerticalThumbDrawable.setBounds(0, 0, mVerticalThumbWidth, mVerticalThumbHeight);
        mVerticalTrackDrawable
                .setBounds(0, 0, mVerticalTrackWidth, mRecyclerViewHeight);

        if (isLayoutRTL()) {
            mVerticalTrackDrawable.draw(canvas);
            canvas.translate(mVerticalThumbWidth, top);
            canvas.scale(-1, 1);
            mVerticalThumbDrawable.draw(canvas);
            canvas.scale(1, 1);
            canvas.translate(-mVerticalThumbWidth, -top);
        } else {
            canvas.translate(left, 0);
            mVerticalTrackDrawable.draw(canvas);

            canvas.translate(0, top);
            mVerticalThumbDrawable.draw(canvas);

            /**
             * Customize the {@link android.support.v7.widget.FastScroller} here.
             * Shows a thumb text representing first completely visible item position in
             * {@link RecyclerView} besides the thumb scroller.
             */
            canvas.translate(-mVerticalThumbTextWidth, 0);
            mVerticalThumbDrawable.setBounds(0, 0, mVerticalThumbTextWidth, mVerticalThumbTextHeight);
            mVerticalThumbDrawable.draw(canvas);
            int position = ((GridLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition() + 1;
            // Only update the thumb text (showing first completely visible item position)
            // if the position is a multiple of 20.
            if (position % 20 == 0) {
                mThumbTextItemPosition = position;
            }
            String thumbTextPosition = String.valueOf(mThumbTextItemPosition);
            float textWidth = mVerticalThumbTextPaint.measureText(thumbTextPosition);
            canvas.drawText(thumbTextPosition,
                    (mVerticalThumbTextWidth - textWidth) / 2f,
                    mVerticalThumbTextHeight / 2f,
                    mVerticalThumbTextPaint);

            canvas.translate(-left + mVerticalThumbTextWidth, -top);
        }
    }

    private void drawHorizontalScrollbar(Canvas canvas) {
        int viewHeight = mRecyclerViewHeight;

        int top = viewHeight - mHorizontalThumbHeight;
        int left = mHorizontalThumbCenterX - mHorizontalThumbWidth / 2;
        mHorizontalThumbDrawable.setBounds(0, 0, mHorizontalThumbWidth, mHorizontalThumbHeight);
        mHorizontalTrackDrawable
                .setBounds(0, 0, mRecyclerViewWidth, mHorizontalTrackHeight);

        canvas.translate(0, top);
        mHorizontalTrackDrawable.draw(canvas);
        canvas.translate(left, 0);
        mHorizontalThumbDrawable.draw(canvas);
        canvas.translate(-left, -top);
    }

    /**
     * Notify the scroller of external change of the scroll, e.g. through dragging or flinging on
     * the view itself.
     *
     * @param offsetX The new scroll X offset.
     * @param offsetY The new scroll Y offset.
     */
    private void updateScrollPosition(int offsetX, int offsetY) {
        int verticalContentLength = mRecyclerView.computeVerticalScrollRange();
        int verticalVisibleLength = mRecyclerViewHeight;
        mNeedVerticalScrollbar = verticalContentLength - verticalVisibleLength > 0
                && mRecyclerViewHeight >= mScrollbarMinimumRange;

        int horizontalContentLength = mRecyclerView.computeHorizontalScrollRange();
        int horizontalVisibleLength = mRecyclerViewWidth;
        mNeedHorizontalScrollbar = horizontalContentLength - horizontalVisibleLength > 0
                && mRecyclerViewWidth >= mScrollbarMinimumRange;

        if (!mNeedVerticalScrollbar && !mNeedHorizontalScrollbar) {
            if (mState != STATE_HIDDEN) {
                setState(STATE_HIDDEN);
            }
            return;
        }

        if (mNeedVerticalScrollbar) {
            float middleScreenPos = offsetY + verticalVisibleLength / 2.0f;
            mVerticalThumbCenterY =
                    (int) ((verticalVisibleLength * middleScreenPos) / verticalContentLength);
//            mVerticalThumbHeight = mContext.getResources().getDimensionPixelOffset(R.dimen
//                    .recycler_grid_view_fast_scroller_thumb_height);
        }

        if (mNeedHorizontalScrollbar) {
            float middleScreenPos = offsetX + horizontalVisibleLength / 2.0f;
            mHorizontalThumbCenterX =
                    (int) ((horizontalVisibleLength * middleScreenPos) / horizontalContentLength);
            mHorizontalThumbWidth = Math.min(horizontalVisibleLength,
                    (horizontalVisibleLength * horizontalVisibleLength) / horizontalContentLength);
        }

        if (mState == STATE_HIDDEN || mState == STATE_VISIBLE) {
            setState(STATE_VISIBLE);
        }
    }

    /**
     * Silently observe and/or take over touch events sent to the RecyclerView
     * before they are handled by either the RecyclerView itself or its child views.
     *
     * <p>The onInterceptTouchEvent methods of each attached OnItemTouchListener will be run
     * in the order in which each listener was added, before any other touch processing
     * by the RecyclerView itself or child views occurs.</p>
     *
     * @param rv RecyclerView this method is intercepting the touch event.
     * @param e  MotionEvent describing the touch event. All coordinates are in
     *           the RecyclerView's coordinate system.
     * @return true if this OnItemTouchListener wishes to begin intercepting touch events, false
     * to continue with the current behavior and continue observing future events in
     * the gesture.
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        final boolean handled;
        if (mState == STATE_VISIBLE) {
            boolean insideVerticalThumb = isPointInsideVerticalThumb(e.getX(), e.getY());
            boolean insideHorizontalThumb = isPointInsideHorizontalThumb(e.getX(), e.getY());
            if (e.getAction() == MotionEvent.ACTION_DOWN
                    && (insideVerticalThumb || insideHorizontalThumb)) {
                if (insideHorizontalThumb) {
                    mDragState = DRAG_X;
                    mHorizontalDragX = (int) e.getX();
                } else if (insideVerticalThumb) {
                    mDragState = DRAG_Y;
                    mVerticalDragY = (int) e.getY();
                }

                setState(STATE_DRAGGING);
                handled = true;
            } else {
                handled = false;
            }
        } else if (mState == STATE_DRAGGING) {
            handled = true;
        } else {
            handled = false;
        }
        return handled;
    }

    /**
     * Process a touch event as part of a gesture that was claimed by returning true from
     * a previous call to {@link #onInterceptTouchEvent}.
     *
     * @param rv RecyclerView this method is handle the touch event.
     * @param e  MotionEvent describing the touch event. All coordinates are in
     */
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        if (mState == STATE_HIDDEN) {
            return;
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            boolean insideVerticalThumb = isPointInsideVerticalThumb(e.getX(), e.getY());
            boolean insideHorizontalThumb = isPointInsideHorizontalThumb(e.getX(), e.getY());
            if (insideVerticalThumb || insideHorizontalThumb) {
                if (insideHorizontalThumb) {
                    mDragState = DRAG_X;
                    mHorizontalDragX = (int) e.getX();
                } else if (insideVerticalThumb) {
                    mDragState = DRAG_Y;
                    mVerticalDragY = (int) e.getY();
                }
                setState(STATE_DRAGGING);
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP && mState == STATE_DRAGGING) {
            mVerticalDragY = 0;
            mHorizontalDragX = 0;
            setState(STATE_VISIBLE);
            mDragState = DRAG_NONE;
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && mState == STATE_DRAGGING) {
            show();
            if (mDragState == DRAG_X) {
                horizontalScrollTo(e.getX());
            }
            if (mDragState == DRAG_Y) {
                verticalScrollTo(e.getY());
            }
        }
    }

    /**
     * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
     * intercept touch events with
     * {@link ViewGroup#onInterceptTouchEvent(MotionEvent)}.
     *
     * @param disallowIntercept True if the child does not want the parent to
     *                          intercept touch events.
     * @see ViewParent#requestDisallowInterceptTouchEvent(boolean)
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }

    private void verticalScrollTo(float y) {
        final int[] scrollbarRange = getVerticalRange();
        y = Math.max(scrollbarRange[0], Math.min(scrollbarRange[1], y));
        if (Math.abs(mVerticalThumbCenterY - y) < 2) {
            return;
        }
        int scrollingBy = scrollTo(mVerticalDragY, y, scrollbarRange,
                mRecyclerView.computeVerticalScrollRange(),
                mRecyclerView.computeVerticalScrollOffset(), mRecyclerViewHeight);
        if (scrollingBy != 0) {
            mRecyclerView.scrollBy(0, scrollingBy);
        }
        mVerticalDragY = y;
    }

    private void horizontalScrollTo(float x) {
        final int[] scrollbarRange = getHorizontalRange();
        x = Math.max(scrollbarRange[0], Math.min(scrollbarRange[1], x));
        if (Math.abs(mHorizontalThumbCenterX - x) < 2) {
            return;
        }

        int scrollingBy = scrollTo(mHorizontalDragX, x, scrollbarRange,
                mRecyclerView.computeHorizontalScrollRange(),
                mRecyclerView.computeHorizontalScrollOffset(), mRecyclerViewWidth);
        if (scrollingBy != 0) {
            mRecyclerView.scrollBy(scrollingBy, 0);
        }

        mHorizontalDragX = x;
    }

    private int scrollTo(float oldDragPos, float newDragPos, int[] scrollbarRange, int scrollRange,
                         int scrollOffset, int viewLength) {
        int scrollbarLength = scrollbarRange[1] - scrollbarRange[0];
        if (scrollbarLength == 0) {
            return 0;
        }
        float percentage = ((newDragPos - oldDragPos) / (float) scrollbarLength);
        int totalPossibleOffset = scrollRange - viewLength;
        int scrollingBy = (int) (percentage * totalPossibleOffset);
        int absoluteOffset = scrollOffset + scrollingBy;
        if (absoluteOffset < totalPossibleOffset && absoluteOffset >= 0) {
            return scrollingBy;
        } else {
            return 0;
        }
    }

    @VisibleForTesting
    private boolean isPointInsideVerticalThumb(float x, float y) {
        return (isLayoutRTL() ? x <= mVerticalThumbWidth / 2
                : x >= mRecyclerViewWidth - mVerticalThumbWidth)
                && y >= mVerticalThumbCenterY - mVerticalThumbHeight / 2
                && y <= mVerticalThumbCenterY + mVerticalThumbHeight / 2;
    }

    @VisibleForTesting
    private boolean isPointInsideHorizontalThumb(float x, float y) {
        return (y >= mRecyclerViewHeight - mHorizontalThumbHeight)
                && x >= mHorizontalThumbCenterX - mHorizontalThumbWidth / 2
                && x <= mHorizontalThumbCenterX + mHorizontalThumbWidth / 2;
    }

    /**
     * Gets the (min, max) vertical positions of the vertical scroll bar.
     */
    private int[] getVerticalRange() {
        mVerticalRange[0] = mMargin;
        mVerticalRange[1] = mRecyclerViewHeight - mMargin;
        return mVerticalRange;
    }

    /**
     * Gets the (min, max) horizontal positions of the horizontal scroll bar.
     */
    private int[] getHorizontalRange() {
        mHorizontalRange[0] = mMargin;
        mHorizontalRange[1] = mRecyclerViewWidth - mMargin;
        return mHorizontalRange;
    }

    private class AnimatorListener extends AnimatorListenerAdapter {

        private boolean mCanceled = false;

        @Override
        public void onAnimationEnd(Animator animation) {
            // Cancel is always followed by a new directive, so don't update state.
            if (mCanceled) {
                mCanceled = false;
                return;
            }
            if ((float) mShowHideAnimator.getAnimatedValue() == 0) {
                mAnimationState = ANIMATION_STATE_OUT;
                setState(STATE_HIDDEN);
            } else {
                mAnimationState = ANIMATION_STATE_IN;
                requestRedraw();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mCanceled = true;
        }
    }

    private class AnimatorUpdater implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int alpha = (int) (SCROLLBAR_FULL_OPAQUE * ((float) valueAnimator.getAnimatedValue()));
            mVerticalThumbDrawable.setAlpha(alpha);
            mVerticalTrackDrawable.setAlpha(alpha);
            mVerticalThumbTextPaint.setAlpha(alpha);
            requestRedraw();
        }
    }
}