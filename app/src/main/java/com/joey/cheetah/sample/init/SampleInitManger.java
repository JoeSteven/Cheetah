package com.joey.cheetah.sample.init;

import com.joey.cheetah.core.init.InitManager;
import com.joey.cheetah.sample.init.task.ApiTask;
import com.joey.cheetah.sample.init.task.BleTask;
import com.joey.cheetah.sample.init.task.DebugTask;
import com.joey.cheetah.sample.init.task.DemoBackgroundTask;
import com.joey.cheetah.sample.init.task.ImageTask;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class SampleInitManger extends InitManager {
    @Override
    public void addTask() {
        add(new DebugTask());
        add(new ApiTask());
        add(new BleTask());
        add(new ImageTask());
        add(new DemoBackgroundTask());
    }
}
