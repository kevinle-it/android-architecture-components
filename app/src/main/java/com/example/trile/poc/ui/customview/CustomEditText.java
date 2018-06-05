package com.example.trile.poc.ui.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * @author trile
 * @since 6/5/18 at 13:14
 */
public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context) {
        super(context);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            dispatchKeyEvent(event);
            this.clearFocus();
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
