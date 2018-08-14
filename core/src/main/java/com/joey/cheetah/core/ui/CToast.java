package com.joey.cheetah.core.ui;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.joey.cheetah.core.utils.Global;
import com.joey.cheetah.core.utils.ResGetter;


/**
 * description - A helper to show toast
 * author:Joey
 * date:2018/7/26
 */
public class CToast {
    private static IToast sDefaultIToast;

    public static void setDefaultIToast(IToast toast) {
        sDefaultIToast = toast;
    }

    public static void show(String message) {
        if (sDefaultIToast == null) {
            sDefaultIToast = new DefaultToast();
        }
        show(sDefaultIToast, message);
    }

    public static void show(int stringRes) {
        show(ResGetter.string(stringRes));
    }

    public static void show(IToast toast, int stringRes) {
        show(toast, ResGetter.string(stringRes));
    }

    public static void show(IToast toast, String message) {
        if (toast != null) {
            toast.show(message);
        }
    }

    public static class DefaultToast implements IToast {
        private String oldToastMsg = null;
        private long oldToastTime = 0;

        @Override
        public void show(String message) {
            long time = System.currentTimeMillis();
            if (oldToastMsg != null) {
                if (oldToastMsg.equals(message)) {
                    if ((time - oldToastTime) < 2000L) {
                        return;
                    }
                }
            }
            oldToastMsg = message;
            oldToastTime = time;
            if (TextUtils.isEmpty(message))
                return;

            Toast toast = Toast.makeText(Global.context(), message,
                    Toast.LENGTH_SHORT);
            //可以控制toast显示的位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
