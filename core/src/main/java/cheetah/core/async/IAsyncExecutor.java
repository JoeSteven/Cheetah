package cheetah.core.async;

import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;

/**
 * Description: an interface to run async task, support multi tasks
 * author:Joey
 * date:2018/7/26
 */
public interface IAsyncExecutor extends LifecycleObserver {

    void execute(AsyncTask task);

    void execute(AsyncTask task, long delay);

    void execute(AsyncTask task, IAsyncCallback callback);

    /**
     * run tasks
     * @param delay unit milliseconds
     * @return task id, to cancel task
     */
    void execute(AsyncTask task, IAsyncCallback callback, long delay);

    <T> void execute(AsyncResultTask<T> task, IAsyncResultCallback<T> callback);

    /**
     * run tasks that will post result to observer,result must not be null
     *
     * @param task
     * @param callback
     * @param <T>
     * @param delay unit milliseconds
     */
    <T> void execute(AsyncResultTask<T> task, IAsyncResultCallback<T> callback, long delay);


    /**
     * clear all tasks
     */
    void clear();

    interface AsyncTask {
        void run() throws Exception;
    }

    interface IAsyncCallback {

        /**
         * call when task executed
         */
        void done();

        void error(Throwable e);
    }

    interface AsyncResultTask<T> {
        @NonNull
        T run() throws Exception;
    }


    interface IAsyncResultCallback<T> {

        /**
         * call when task executed
         */
        void done(@NonNull T t);

        void error(Throwable e);
    }
}
