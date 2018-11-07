package com.joey.cheetah.sample.init.task;

import com.joey.cheetah.core.init.InitTask;
import com.joey.cheetah.core.media.ImageHelper;
import com.joey.cheetah.core.utils.CLog;
import com.joey.cheetah.core.global.Global;

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
public class ImageTask extends InitTask {
    @Override
    protected void execute() {
        CLog.d("image_init", "init");
        ImageHelper.init(Global.context());
    }

    @Override
    public Priority priority() {
        return Priority.EMERGENCY;
    }
}
