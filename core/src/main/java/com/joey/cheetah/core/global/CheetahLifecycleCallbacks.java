package com.joey.cheetah.core.global;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class CheetahLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{
    private Activity topActivity;
    private static CheetahLifecycleCallbacks lifecycleCallbacks = new CheetahLifecycleCallbacks();
    public static CheetahLifecycleCallbacks inst() {
        return lifecycleCallbacks;
    }


    private CheetahLifecycleCallbacks() { }

    public Activity getTopActivity() {
        return topActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        topActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        topActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
