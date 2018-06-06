package com.example.trile.poc.ui.customview.chip;

import android.widget.TextView;

/**
 * @author trile
 * @since 6/6/18 at 15:25
 */
public interface OnChipStateChangeListener {
    void onStateChanged(Chip chip, Chip.State state);
}
