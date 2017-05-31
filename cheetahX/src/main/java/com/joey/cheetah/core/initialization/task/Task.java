package com.joey.cheetah.core.initialization.task;

public interface Task extends Runnable {
    enum Priority {
        EMERGENCY,
        URGENT,
        BACKGROUND,
        DELAYED,
        DELAYED_BACKGROUND
    }

    Priority getPriority();

    long getDuration();

    boolean isMainProcessOnly();

}
