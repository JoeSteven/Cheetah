package com.joey.cheetah.core.utils;

/**
 * Description: util for ui
 * author:Joey
 * date:2018/7/30
 */
public class UIUtil {

    public static int screenWitdh() {
        return ResGetter.resources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight() {
        return ResGetter.resources().getDisplayMetrics().heightPixels;
    }

    /**
     * dp to px
     */
    public static int dp2px(int dpValue) {
        final float scale = ResGetter.resources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     */
    public static int px2dp(int pxValue) {
        final float scale = ResGetter.resources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(int spValue) {
        final float scale = ResGetter.resources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static int px2sp(int pxValue) {
        final float scale = ResGetter.resources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }
}
