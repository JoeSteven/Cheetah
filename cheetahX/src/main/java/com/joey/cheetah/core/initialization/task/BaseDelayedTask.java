package com.joey.cheetah.core.initialization.task;


public abstract class BaseDelayedTask extends BaseTask {
    @Override
    public Priority getPriority() {
        return Priority.DELAYED;
    }
}
