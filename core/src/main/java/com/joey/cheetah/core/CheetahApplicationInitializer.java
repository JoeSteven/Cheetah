package com.joey.cheetah.core;

import android.content.Context;

import com.joey.cheetah.core.init.InitManager;
import com.joey.cheetah.core.utils.Global;

/**
 * Description: Initializer for Application
 * author:Joey
 * date:2018/8/14
 */
public class CheetahApplicationInitializer {
    private static InitManager sInitManger;

    /**
     * invoke in Application.attachBaseContext
     * @param context Application
     * @param manager InitManager
     */
    public static void attachBaseContext(Context context, InitManager manager) {
        Global.initContext(context);
        sInitManger = manager;
        sInitManger.addTask();
    }


    /**
     * invoke before super.onCreate()
     */
    public static void beforeSuperOnCreate() {
        if (sInitManger != null) {
            sInitManger.beforeOnCreate();
        }
    }

    /**
     * invoke after super.onCreate()
     */
    public static void afterSuperOnCreate() {
        if (sInitManger != null) {
            sInitManger.afterOnCreate();
        }
    }
}
