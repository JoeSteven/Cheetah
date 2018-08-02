package cheetah.core.init;

import cheetah.core.async.IAsyncExecutor;
import cheetah.core.utils.CLog;
import cheetah.core.utils.Global;

/**
 * Description: task need to init when application create
 * author:Joey
 * date:2018/7/25
 */
public abstract class InitTask implements IAsyncExecutor.AsyncTask {
    private long duration = -1;
    private boolean done = false;

    public enum Type {
        EMERGENCY,// run before application super.onCreate
        URGENT,// run after application super.onCreate
        BACKGROUND// run in background thread
    }

    public void run() {
        long start = System.currentTimeMillis();
        execute();
        duration = System.currentTimeMillis() - start;
        done = true;
        if (Global.debug() && type() != Type.BACKGROUND) {
            printDuration();
        }
    }

    private void printDuration() {
        String msg = type() + " task " + name() + " duration ===>" + duration();
        if (duration < 200) {
            CLog.d(CLog.LOG_INIT, msg);
        } else if (duration > 200) {
            CLog.e(CLog.LOG_INIT, msg);
        }
    }

    public long duration() {
        return duration;
    }

    public boolean done() {
        return done;
    }

    protected abstract void execute();

    public abstract Type type();

    public String name() {
        return getClass().getSimpleName();
    }
}
