package com.terminus.ereboot

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Description:
 * author:Joey
 * date:2018/9/26
 */
internal class ExceptionLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
        ERebootUncaughtExceptionHandler.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        ERebootUncaughtExceptionHandler.add(activity)
    }


}