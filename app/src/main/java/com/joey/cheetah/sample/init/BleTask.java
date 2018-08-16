package com.joey.cheetah.sample.init;

import com.joey.cheetah.core.init.InitTask;
import com.joey.cheetah.core.global.Global;
import com.joey.cheetah.sample.ble.BlePermissionRequester;
import com.joey.rxble.RxBle;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class BleTask extends InitTask{

    @Override
    protected void execute() {
        RxBle.init(Global.context(), new BlePermissionRequester());
        RxBle.enableLog(Global.debug());
    }

    @Override
    public Priority priority() {
        return Priority.URGENT;
    }
}
