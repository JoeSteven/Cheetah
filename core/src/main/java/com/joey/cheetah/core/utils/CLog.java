package com.joey.cheetah.core.utils;

import android.util.Log;

/**
 * Description: An util to print log for project
 * author:Joey
 * date:2018/7/25
 */
public class CLog {
    public static String LOG_API = "clog_api";//api log
    public static String LOG_INIT = "clog_init";//init task log

    private volatile static boolean force = false;

    private static boolean debug = false;

    public static void debug(boolean isDebug) {
        debug = isDebug;
    }

    /**
     * force to print log for release package
     *
     * @param isForceLog
     */
    public static void forceLog(boolean isForceLog) {
        force = isForceLog;
    }

    public static void v(String tag, String msg) {
        if (debug || force) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (debug || force) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (debug || force) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (debug || force) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (debug || force) {
            Log.e(tag, msg);
        }
    }

    public static String className(Object object) {
        if (object == null) return "null class";
        return object.getClass().getSimpleName();
    }

}
