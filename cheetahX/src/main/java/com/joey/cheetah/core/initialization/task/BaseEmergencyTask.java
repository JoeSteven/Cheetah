package com.joey.cheetah.core.initialization.task;


public abstract class BaseEmergencyTask extends BaseTask {

    @Override
    public Priority getPriority() {
        return Priority.EMERGENCY;
    }
}
