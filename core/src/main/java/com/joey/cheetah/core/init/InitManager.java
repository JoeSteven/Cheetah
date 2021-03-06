package com.joey.cheetah.core.init;

import java.util.ArrayList;
import java.util.List;

import com.joey.cheetah.core.async.AsyncManger;
import com.joey.cheetah.core.async.IAsyncExecutor;

/**
 * Description: manage tasks which need to init when application create
 * author:Joey
 * date:2018/7/25
 */
public abstract class InitManager {
    private List<InitTask> emergencyTasks = new ArrayList<>();
    private List<InitTask> urgentTasks = new ArrayList<>();
    private List<InitTask> backgroundTasks = new ArrayList<>();
    IAsyncExecutor mAsyncExecutor;

    public InitManager() {
        mAsyncExecutor = asyncExecutor();
    }

    /**
     * add all your init tasks in this method
     * remember all your tasks will be run one by one in list
     * don't need to call this method,already called in CheetahApplicationInitializer.attachBaseContext()
     */
    public abstract void addTask();

    /**
     * call this method to add task
     *
     * @param task init task
     */
    public void add(InitTask task) {
        switch (task.priority()) {
            case EMERGENCY:
                emergencyTasks.add(task);
                break;
            case URGENT:
                urgentTasks.add(task);
                break;
            case BACKGROUND:
                backgroundTasks.add(task);
                break;
            default:
                throw new IllegalArgumentException(task.name() +
                        " priority is illegal! Priority must be EMERGENCY, URGENT or BACKGROUND !");
        }
    }

    /**
     * call this method in Application.onCreate() and before super.onCreate()
     */
    public void beforeOnCreate() {
        for (InitTask task : emergencyTasks) {
            task.run();
            monitorTask(task);
        }
    }

    /**
     * call this method in Application.onCreate() and after super.onCreate()
     */
    public void afterOnCreate() {
        for (InitTask task : urgentTasks) {
            task.run();
            monitorTask(task);
        }
        for (InitTask task : backgroundTasks) {
            mAsyncExecutor.execute(task);
        }
    }


    /**
     * @param task use to monitor task
     */
    protected void monitorTask(InitTask task) {

    }

    protected IAsyncExecutor asyncExecutor() {
        return AsyncManger.obtain();
    }

}
