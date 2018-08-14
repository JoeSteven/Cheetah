package com.joey.cheetah.sample.init;

import com.joey.cheetah.core.init.InitManager;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class SampleInitManger extends InitManager {
    @Override
    public void addTask() {
        add(new ApiTask());
        add(new BleTask());
        add(new ImageTask());
        add(new DemoBackgroundTask());
    }
}
