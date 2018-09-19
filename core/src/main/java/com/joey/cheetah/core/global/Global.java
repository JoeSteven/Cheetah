package com.joey.cheetah.core.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.joey.cheetah.core.BuildConfig;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

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

    /**
     * @return version code for package
     */
    public static int versionCode(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) return -1;
        try {
            return sContext.getPackageManager().getPackageInfo(pkgName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    /**
     * @return version name for package
     */
    public static String versionName(String pkgName) {
        if (TextUtils.isEmpty(pkgName)) return "-1";
        try {
            return sContext.getPackageManager().getPackageInfo(pkgName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return  "-1";
        }
    }

    /**
     * @return is app foreground
     */
    public static boolean isAppForeground(String pkgName){
        if (TextUtils.isEmpty(pkgName)) return false;
        ActivityManager manager = (ActivityManager) sContext.getSystemService(ACTIVITY_SERVICE);
        if (manager == null) return false;
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info :infos) {
            return TextUtils.equals(info.processName, pkgName);
        }
        return false;
    }
}
