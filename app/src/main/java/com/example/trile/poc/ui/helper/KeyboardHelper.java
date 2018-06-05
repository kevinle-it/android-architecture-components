package com.example.trile.poc.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.trile.poc.utils.Objects;

/**
 * Created by lmtri on 6/13/2017.
 */

public class KeyboardHelper {
    public static void showSoftKeyboard(@NonNull Activity activity, @NonNull EditText editText) {
        Objects.requireNonNull(activity, "Cannot Show Soft Keyboard with NULL Activity/Context!");
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.post(() -> {
            editText.requestFocus();
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    public static void hideSoftKeyboard(@NonNull Activity activity, boolean clearCurrentFocus) {
        Objects.requireNonNull(activity, "Cannot Hide Soft Keyboard with NULL Activity/Context!");
        // Check if no view has focus.
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (clearCurrentFocus) {
                view.clearFocus();
            }
        }
    }
}
