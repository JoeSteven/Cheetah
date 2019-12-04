package com.joey.cheetah.sample.java;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.annotation.NonNull;

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
