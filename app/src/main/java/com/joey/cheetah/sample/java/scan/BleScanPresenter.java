package com.joey.cheetah.sample.java.scan;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.joey.cheetah.mvp.AbsPresenter;
import com.joey.rxble.RxBle;
import com.joey.rxble.RxBleOperator;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class BleScanPresenter extends AbsPresenter<IBleScanView>{
    private RxBleOperator mOperator;
    public BleScanPresenter(IBleScanView view) {
        super(view);
        mOperator = RxBle.create();
    }

    public void mark() {
        RxBle.markState();
    }

    public void restore() {
        RxBle.restoreState();
    }

    @SuppressLint("CheckResult")
    public void scan() {
        mOperator.scan()
                .subscribe(result -> {if (isValid()) mView.refresh(result);},
                        throwable -> {if (isValid()) mView.toast("scan failed!");});
    }

    @SuppressLint("CheckResult")
    public void open() {
        mOperator.enable()
                .subscribe(client -> {if (isValid())mView.toast("open bluetooth success!");},
                        throwable -> {if (isValid())mView.toast("open bluetooth failed!");});
    }

    public void close() {
        mOperator.disable();
        if (isValid()) mView.toast("close blue tooth success!");
    }

    @Override
    public void onDestroyed(@NonNull LifecycleOwner owner) {
        super.onDestroyed(owner);
        mOperator.release();
    }

    @Override
    public void onSaveData(Bundle outState) {

    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {

    }
}
