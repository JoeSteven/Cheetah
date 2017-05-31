package com.joey.cheetah.core.initialization.task;


/**
 * Created by jia on 17/4/7.
 */

 abstract class BaseDelayedBackTask extends BaseTask {

    @Override
    public Priority getPriority() {
        return Priority.DELAYED_BACKGROUND;
    }
}
