package com.joey.cheetah.core.global;

import android.app.Activity;
import android.content.Context;

import com.joey.cheetah.core.BuildConfig;

/**
 * Description: Global config or attribute for project
 * author:Joey
 * date:2018/7/25
 */
public class Global {
    private static Context sContext;
    private static boolean sDebug = BuildConfig.DEBUG;

    /**
     * @param context application context
     */
    public static void initContext(Context context) {
        if (context != null) {
            sContext = context;
        }
    }

    /**
     * @return an application context or null
     * don't need to protected when returns null,just let it crash
     * because only the application is not running can cause null
     */
    public static Context context() {
        return sContext;
    }

    /**
     * get the top activity in task
     */
    public static Activity topActivity() {
        return CheetahLifecycleCallbacks.inst().getTopActivity();
    }

    /**
     * @return is project running as debug mode
     */
    public static boolean debug() {
        return sDebug;
    }

}