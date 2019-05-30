package com.joey.cheetah.core.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.AnimatorRes;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;


/**
 * Description: An resource getter
 * author:Joey
 * date:2018/7/26
 */
public class ResGetter {
    private static Context sContext = null;

    public static void init(Context globalContext) {
        sContext = globalContext;
    }

    public static Resources resources() {
        return sContext.getResources();
    }

    public static String string(@StringRes int resID) {
        return resources().getString(resID);
    }

    public static String stringFormat(@StringRes int resID, Object... args) {
        return resources().getString(resID, args);
    }

    public static XmlResourceParser animation(@AnimatorRes int resId) {
        return resources().getAnimation(resId);
    }

    public static AssetManager assets() {
        return resources().getAssets();
    }

    public static boolean boolea(@BoolRes int resID) {
        return resources().getBoolean(resID);
    }

    public static int color(@ColorRes int resID) {
        return resources().getColor(resID);
    }

    public static ColorStateList colorStateList(int resID) {
        return resources().getColorStateList(resID);
    }

    public static Configuration configuration() {
        return resources().getConfiguration();
    }

    public static float dimension(@DimenRes int resID) {
        return resources().getDimension(resID);
    }

}
