package com.joey.cheetah.sample.init;

import com.joey.cheetah.core.init.InitTask;
import com.joey.cheetah.sample.Api.Api;

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
public class ApiTask extends InitTask{
    @Override
    protected void execute() {
        Api.INSTANCE.init();
    }

    @Override
    public Priority priority() {
        return Priority.EMERGENCY;
    }
}
