package com.joey.cheetah.core.initialization.task;


public abstract class BaseBackgroundTask extends BaseTask {
    @Override
    public Priority getPriority() {
        return Priority.BACKGROUND;
    }
}
