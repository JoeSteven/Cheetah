package com.joey.cheetah.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.joey.cheetah.core.async.IAsyncExecutor;
import com.joey.cheetah.core.async.IBusStop;
import com.joey.cheetah.core.async.RxJavaManager;

import io.reactivex.disposables.Disposable;

/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class AbsPresenter<T extends IView> implements LifecycleObserver {
    protected @Nullable T mView;
    private RxJavaManager mRxJavaManager;

    public AbsPresenter(@NonNull T view) {
        mView = view;
        mView.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(@NonNull LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(@NonNull LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(@NonNull LifecycleOwner owner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroyed(@NonNull LifecycleOwner owner) {
        // destroy view to void memory leak
        if (mRxJavaManager != null) {
            mRxJavaManager.clear();
        }
        mView = null;
    }

    /**
     * add a disposable to manage
     */
    protected void add(Disposable disposable) {
        manager().add(disposable);
    }

    /**
     * get async executor
     */
    protected IAsyncExecutor async() {
        return manager();
    }

    /**
     * get BusStop to register event or post event
     */
    protected IBusStop bus() {
        return manager();
    }

    protected RxJavaManager manager() {
        if (mRxJavaManager == null) {
            mRxJavaManager = new RxJavaManager();
        }
        return mRxJavaManager;
    }

    public abstract void onSaveData(Bundle outState);

    public abstract void onRestoredData(Bundle savedInstanceState);

    protected boolean isValid() {
        return mView != null;
    }
}
