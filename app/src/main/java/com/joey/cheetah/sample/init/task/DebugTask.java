package com.joey.cheetah.sample.init.task;

import com.joey.cheetah.core.init.InitTask;
import com.joey.cheetah.core.utils.CLog;
import com.joey.cheetah.sample.BuildConfig;

/**
 * Description:
 * author:Joey
 * date:2018/9/4
 */
public class DebugTask extends InitTask {
    @Override
    protected void execute() {
        CLog.debug(BuildConfig.DEBUG);
    }

    @Override
    public Priority priority() {
        return Priority.EMERGENCY;
    }
}
