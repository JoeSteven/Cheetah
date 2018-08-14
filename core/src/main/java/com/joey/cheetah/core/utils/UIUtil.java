package com.joey.cheetah.core.utils;

import android.content.Context;

/**
 * Description: util for ui
 * author:Joey
 * date:2018/7/30
 */
public class UIUtil {
    /**
     * dp to px
     */
    public static int dip2px(int dpValue) {
        final float scale = ResGetter.resources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     */
    public static int px2dip(Context context, int pxValue) {
        final float scale = ResGetter.resources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
