package com.joey.cheetah.core.initialization;


import com.joey.cheetah.undo.AsyncManager;
import com.joey.cheetah.core.initialization.task.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class InitializationManager {

    protected List<Task> mBeforeApplicationCreateTasks = new ArrayList<>();
    protected List<Task> mAfterApplicationPostCreateTasks = new ArrayList<>();
    protected List<Task> mDelayedTasks = new ArrayList<>();
    protected long mBeginTime;
    protected boolean mIsMainProcess;
    protected boolean mUiHasShown;
    protected AsyncManager mAsyncManager;

    public InitializationManager() {
    }

    public void init(boolean isMainProcess) {
        this.mIsMainProcess = isMainProcess;
        this.mAsyncManager = getAsyncManager();
        initAllTask();
    }

    protected abstract AsyncManager getAsyncManager();

    protected abstract void initAllTask();

    public long getBeginTime() {
        return mBeginTime;
    }

    public void beforeApplicationCreate() {
        mBeginTime = System.currentTimeMillis();

        while (!mBeforeApplicationCreateTasks.isEmpty()) {
            Task task = mBeforeApplicationCreateTasks.remove(0);
            task.run();
        }
    }

    public void onUIShown() {
        if (mUiHasShown) {
            return;
        }

        mUiHasShown = true;

        while (!mDelayedTasks.isEmpty()) {
            Task task = mDelayedTasks.remove(0);
            switch (task.getPriority()) {
                case DELAYED:
                    task.run();
                    break;
                case DELAYED_BACKGROUND:
                    scheduleTaskBackground(task);
                    break;
                default:
                    break;
            }
        }
    }

    public void afterApplicationCreate() {
        while (!mAfterApplicationPostCreateTasks.isEmpty()) {
            Task task = mAfterApplicationPostCreateTasks.remove(0);
            switch (task.getPriority()) {
                case URGENT:
                    task.run();
                    break;
                case BACKGROUND:
                    scheduleTaskBackground(task);
                    break;
                default:
                    break;
            }
        }
    }

    protected void scheduleTaskBackground(Task task) {
        if (task == null) {
            return;
        }
        mAsyncManager.async(task);
    }

    protected boolean isTaskAllowed(Task task) {
        if (task == null) {
            return false;
        }

        if (task.isMainProcessOnly() && !mIsMainProcess) {
            return false;
        }

        return true;
    }

    protected void addTask(Task task) {
        if (!isTaskAllowed(task)) {
            return;
        }

        switch (task.getPriority()) {
            case EMERGENCY:
                mBeforeApplicationCreateTasks.add(task);
                break;
            case URGENT:
            case BACKGROUND:
                mAfterApplicationPostCreateTasks.add(task);
                break;
            case DELAYED_BACKGROUND:
            case DELAYED:
                mDelayedTasks.add(task);
            default:
                break;
        }
    }

}
