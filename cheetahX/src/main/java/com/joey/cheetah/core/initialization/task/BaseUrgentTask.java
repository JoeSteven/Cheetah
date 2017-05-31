package com.joey.cheetah.core.initialization.task;


public abstract class BaseUrgentTask extends BaseTask {
    @Override
    public Priority getPriority() {
        return Priority.URGENT;
    }
}
