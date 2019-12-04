package com.joey.cheetah.sample.init.task;

import android.app.Application;

import com.joey.cheetah.core.global.Global;
import com.joey.cheetah.core.init.InitTask;
import com.joey.cheetah.core.utils.CLog;
import com.joey.cheetah.sample.BuildConfig;
import com.joey.cheetah.sample.MainActivity;
import com.joey.cheetah.sample.exception.RebootHelper;
import com.terminus.ereboot.ERebootConfig;
import com.terminus.ereboot.ERebootUncaughtExceptionHandler;

/**
 * Description:
 * author:Joey
 * date:2018/9/4
 */
public class RebootTask extends InitTask {
    @Override
    protected void execute() {
        ERebootUncaughtExceptionHandler.INSTANCE.init(ERebootConfig.Companion.with(((Application) Global.context()), MainActivity.class)
                .setOnExceptionOccurListener(RebootHelper.INSTANCE)
                .setDebugMode(true, BuildConfig.DEBUG)
                .setLogFile("test.log", Global.context().getExternalCacheDir().getPath()+"/t-log/")
                .create());
    }

    @Override
    public Priority priority() {
        return Priority.EMERGENCY;
    }
}
