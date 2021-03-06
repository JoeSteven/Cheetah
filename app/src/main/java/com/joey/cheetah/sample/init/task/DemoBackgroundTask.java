package com.joey.cheetah.sample.init.task;

import com.joey.cheetah.core.init.InitTask;
import com.joey.cheetah.core.utils.CLog;

/**
 * Description:
 * author:Joey
 * date:2018/8/2
 */
public class DemoBackgroundTask extends InitTask {
    @Override
    protected void execute() {
        CLog.d("back_ground", "demo task thread is :" + Thread.currentThread());
    }

    @Override
    public Priority priority() {
        return Priority.BACKGROUND;
    }
}
