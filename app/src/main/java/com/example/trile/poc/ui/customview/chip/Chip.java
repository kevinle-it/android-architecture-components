package com.example.trile.poc.ui.customview.chip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.example.trile.poc.R;
import com.example.trile.poc.utils.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author trile
 * @since 6/6/18 at 11:42
 */
public class Chip extends AppCompatTextView implements View.OnTouchListener {

    public static final String TAG = Chip.class.getSimpleName();

    public enum State {
        STATE_IGNORE,
        STATE_INCLUDE,
        STATE_EXCLUDE
    }

    private State mState;

    private int mIgnoreStateTextColor;
    private Drawable mIgnoreStateBackground;

    private int mIncludeStateTextColor;
    private Drawable mIncludeStateBackground;

    private int mExludeStateTextColor;
    private Drawable mExcludeStateBackground;

    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private boolean mTouchEventEnabled = false;

    private List<OnChipStateChangeListener> mOnChipStateChangeListeners;

    public Chip(Context context) {
        super(context);
        setUpControl();
    }

    public Chip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpControl();
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpControl();
    }

    private void setUpControl() {
        setUpView();
        setUpListener();
        setupInitialState();
    }

    private void setUpView() {
        // Setting margin as following is not working.
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        int margin = getResources().getDimensionPixelSize(R.dimen.chip_margin);
//        params.setMargins(margin, margin, margin, margin);
//        this.setLayoutParams(params);

        this.setMaxLines(1);
        this.setEllipsize(TextUtils.TruncateAt.END);

        this.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);

        int padding = getResources().getDimensionPixelSize(R.dimen.chip_padding);
        this.setPadding(padding, padding, padding, padding);

        this.setGravity(Gravity.CENTER);

        this.setClickable(mTouchEventEnabled);

        mIgnoreStateTextColor = ContextCompat.getColor(getContext(), R.color.chip_default_text);
        this.setTextColor(mIgnoreStateTextColor);

        mIncludeStateTextColor = mExludeStateTextColor =
                ContextCompat.getColor(getContext(), R.color.colorPrimary);

        if (mTouchEventEnabled) {
            mIgnoreStateBackground = ContextCompat.getDrawable(getContext(), R.drawable
                    .ripple_chip_ignore_state);
        } else {
            mIgnoreStateBackground = ContextCompat.getDrawable(getContext(), R.drawable
                    .rounded_corners_chip_ignore_state);
        }
        mIncludeStateBackground = ContextCompat.getDrawable(getContext(), R.drawable
                .ripple_chip_include_state);
        mExcludeStateBackground = ContextCompat.getDrawable(getContext(), R.drawable
                .ripple_chip_exclude_state);

//        DrawableCompat.setTint(mIncludeStateBackground, ContextCompat.getColor(getContext(), R
//                .color.colorSecondary));
//        DrawableCompat.setTint(mExcludeStateBackground, ContextCompat.getColor(getContext(), R
//                .color.colorAccent));

        // TODO: 6/6/18 Create Ripple Effect for 3 States.
        // Using <bitmap> android:tint result in Drawable Not Found Exception when getting Drawable.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            TypedValue outValue = new TypedValue();
//            getContext().getTheme().resolveAttribute(
//                    android.R.attr.selectableItemBackground,
//                    outValue,
//                    true
//            );
//            this.setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));
//        }
        this.setBackground(mIgnoreStateBackground);
    }

    private void setUpListener() {
        mOnChipStateChangeListeners = new ArrayList<>();

        super.setOnTouchListener(this);
    }

    private void setupInitialState() {
        mState = State.STATE_IGNORE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // widthMeasureSpec & heightMeasureSpec are from Parent Layout.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void onTouchDown(MotionEvent motionEvent) {
        // Do Nothing.
    }

    private void onTouchUp(MotionEvent motionEvent) {
        nextState();
        // Handle user defined click listeners
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }

    public void nextState() {
        switch (mState) {
            case STATE_IGNORE:
                mState = State.STATE_INCLUDE;
                setIncludeState();
                break;
            case STATE_INCLUDE:
                mState = State.STATE_EXCLUDE;
                setExcludeState();
                break;
            case STATE_EXCLUDE:
                mState = State.STATE_IGNORE;
                setIgnoreState();
                break;
            default:
                throw new AssertionError("Unknown State " + mState);
        }
        dispatchStateChangedEvent();
    }

    private void dispatchStateChangedEvent() {
        if (Objects.nonNull(mOnChipStateChangeListeners) &&
                !mOnChipStateChangeListeners.isEmpty()) {
            for (OnChipStateChangeListener listener : mOnChipStateChangeListeners) {
                listener.onStateChanged(this, mState);
            }
        }
    }

    private void setIgnoreState() {
        if (this.getText().charAt(0) == '-') {
            this.setText(this.getText().toString().substring(1));
            this.setTextColor(mIgnoreStateTextColor);
            this.setBackground(mIgnoreStateBackground);
        }
    }

    private void setIncludeState() {
        this.setText("+" + this.getText().toString());
        this.setTextColor(mIncludeStateTextColor);
        this.setBackground(mIncludeStateBackground);
    }

    private void setExcludeState() {
        this.setText("-" + this.getText().toString().substring(1));
        this.setTextColor(mExludeStateTextColor);
        this.setBackground(mExcludeStateBackground);
    }

    public State getState() {
        return mState;
    }

    public void setTouchEventEnabled(boolean enabled) {
        mTouchEventEnabled = enabled;
        this.setClickable(mTouchEventEnabled);

        if (mTouchEventEnabled) {
            mIgnoreStateBackground = ContextCompat.getDrawable(getContext(), R.drawable
                    .ripple_chip_ignore_state);
        } else {
            mIgnoreStateBackground = ContextCompat.getDrawable(getContext(), R.drawable
                    .rounded_corners_chip_ignore_state);
        }

        this.setBackground(mIgnoreStateBackground);
    }

    public void addOnChipStateChangeListener(OnChipStateChangeListener listener) {
        mOnChipStateChangeListeners.add(listener);
    }

    public void removeOnChipStateChangeListener(OnChipStateChangeListener listener) {
        mOnChipStateChangeListeners.remove(listener);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        Log.d(TAG, "setOnTouchListener: ");
        mOnTouchListener = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mTouchEventEnabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
        return false;
    }
}
