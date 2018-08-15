package com.joey.cheetah.sample.java;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.joey.cheetah.mvp.AbsPresenter;
import com.joey.cheetah.mvp.IView;
import com.joey.rxble.RxBle;
import com.joey.rxble.RxBleOperator;

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
public abstract class AbsBlePresenter<T extends IView> extends AbsPresenter<T> {
    protected RxBleOperator mOperator;

    public AbsBlePresenter(T view) {
        super(view);
        mOperator = RxBle.create();
    }

    public void mark() {
        RxBle.markState();
    }

    public void restore() {
        RxBle.restoreState();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    @Override
    public void onDestroyed(@NonNull LifecycleOwner owner) {
        super.onDestroyed(owner);
        operator().release();
    }

    protected RxBleOperator operator() {
        return mOperator;
    }
}
