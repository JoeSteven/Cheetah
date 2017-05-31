package com.joey.cheetah.helper;


import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;


/**
 * description - resource获取帮助类
 * <p/>
 * author - Joe.
 * create on 16/7/16.
 * change
 * change on .
 */
public class ResourceHelper {
    public static String getString(Context context, @StringRes int id){
        return context.getResources().getString(id);
    }

    public static String getFormatString(Context context, @StringRes int id, Object...s){
        return String.format(context.getResources().getString(id),s);
    }

    public static float getDimen(Context context, @DimenRes int id){
        return context.getResources().getDimension(id);
    }

    public static float getDimenPixel(Context context, @DimenRes int id){
        return context.getResources().getDimensionPixelSize(id);
    }


}
