package cheetah.core.utils;

import android.util.Log;

import cheetah.core.BuildConfig;

/**
 * Description: An util to print log for project
 * author:Joey
 * date:2018/7/25
 */
public class CLog {
    public static String LOG_API = "clog_api";//api log
    public static String LOG_INIT = "clog_init";//init task log

    private volatile static boolean force = false;

    private static boolean debug = BuildConfig.DEBUG || force;

    /**
     * force to print log for release package
     *
     * @param isForceLog
     */
    public static void forceLog(boolean isForceLog) {
        force = isForceLog;
    }

    public static void v(String tag, String msg) {
        if (debug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (debug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (debug) {
            Log.e(tag, msg);
        }
    }

    public static String className(Object object) {
        if (object == null) return "null class";
        return object.getClass().getSimpleName();
    }

}
