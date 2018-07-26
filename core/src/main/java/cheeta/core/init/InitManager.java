package cheeta.core.init;

import java.util.ArrayList;
import java.util.List;

import cheeta.core.async.IAsyncExecutor;
import cheeta.core.async.RxJavaExecutor;

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
     * don't need to call this method,already called in CheetahApplication.attachBaseContext()
     */
    public abstract void addTask();

    /**
     * call this method to add task
     *
     * @param task init task
     */
    public void add(InitTask task) {
        switch (task.type()) {
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
                        " type is illegal! Type must be EMERGENCY, URGENT or BACKGROUND !");
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
        return new RxJavaExecutor();
    }

}
