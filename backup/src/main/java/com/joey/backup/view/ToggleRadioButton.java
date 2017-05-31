package com.joey.backup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by zys on 16/10/12.
 */

public class ToggleRadioButton extends RadioButton {

    public ToggleRadioButton(Context context) {
        super(context);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
