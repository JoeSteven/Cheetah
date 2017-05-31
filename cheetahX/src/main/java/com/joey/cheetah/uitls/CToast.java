package com.joey.cheetah.uitls;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.joey.cheetah.core.CheetahApplication;

/**
 * description - Toast帮助类
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public class CToast {
    private static IToast sIToast;

    public static void setIToast(IToast toast) {
        sIToast = toast;
    }

    public static void show(IToast toast, String message) {
        if (toast != null) {
            toast.show(message);
        }
    }

    public static void show(String message) {
        if (sIToast == null) {
            sIToast = new DefaultToast();
        }
        sIToast.show(message);
    }

    public static class DefaultToast implements IToast{
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

            Toast toast = Toast.makeText(CheetahApplication.getInstance(),message,
                    Toast.LENGTH_SHORT);
            //可以控制toast显示的位置
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
