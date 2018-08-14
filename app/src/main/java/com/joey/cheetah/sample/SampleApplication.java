package com.joey.cheetah.sample;

import android.app.Application;
import android.content.Context;

import com.joey.cheetah.core.CheetahApplicationInitializer;
import com.joey.cheetah.sample.init.SampleInitManger;

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
public class SampleApplication extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CheetahApplicationInitializer.attachBaseContext(this, new SampleInitManger());
    }

    @Override
    public void onCreate() {
        CheetahApplicationInitializer.beforeSuperOnCreate();
        super.onCreate();
        CheetahApplicationInitializer.afterSuperOnCreate();
    }
}
