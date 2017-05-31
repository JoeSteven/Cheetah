package com.joey.cheetah.core.initialization.task;


public abstract class BaseTask implements Task {
    private long mDuration;
    private boolean mFinished;

    @Override
    public long getDuration() {
        if (mFinished) {
            return mDuration;
        }
        return -1L;
    }

    public String getName() {
        return getPriority() + ":" + getClass().getSimpleName();
    }

    @Override
    public void run() {
        long beginTime = 0;
        execute();
        mDuration = System.currentTimeMillis() - beginTime;
        mFinished = true;
    }

    protected abstract void execute();

    @Override
    public boolean isMainProcessOnly() {
        return true;
    }

}
