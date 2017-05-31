package com.joey.cheetah.uitls;

import android.content.Context;

/**
 * description - dp与px相互转换的工具类
 *
 * author - Joe.
 * create on 2016/3/11.
 * change
 * change on .
 */
public class DPUtils {
        /**
         * dp和px的转换
         */
        public static float dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return  (dpValue * scale + 0.5f);
        }

        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

}
