package com.joey.cheetah.uitls;

/**
 * Describe 判断连续点击的工具类
 * 1.默认双击间隔为 500 ms
 * 2.setDefaultDuration() 会改变默认间隔,所有使用 isDoubleClick(int viewId) 都使用默认间隔
 * 3.isDoubleClick(int viewId,long duration) 使用自定义间隔
 * Author Joe
 * created at 16/11/21.
 */
public class DoubleClick {
    private static int sTag = -1;
    private static long sLastClick = 0;
    private static long sDuration = 500;

    /**
     * update the tag
     */
    private static void updateTag(int viewId){
        if(sTag != viewId){
            sTag = viewId;
            sLastClick = 0;
        }
    }

    /**
     * default duration
     * params viewID = R.id.xxx
     */
    public static boolean isDoubleClick(int viewId){
        updateTag(viewId);
        return compare(sDuration);
    }

    /**
     * custom duration
     * params duration = 500
     */
    public static boolean isDoubleClick(int viewId,long duration){
        updateTag(viewId);
        return compare(duration);
    }

    /**
     * compare the duration between two clicks
     */
    private static boolean compare(long duration) {
        long currentClick = System.currentTimeMillis();
        boolean isDouble = (currentClick - sLastClick)<duration;
        sLastClick = currentClick;
        return isDouble;
    }
    /**
     * set the default duration
     */
    public static void setDefaultDuration(long duration){
        sDuration = duration;
    }
}
