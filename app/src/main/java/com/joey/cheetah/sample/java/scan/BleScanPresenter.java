package com.joey.cheetah.sample.java.scan;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.joey.cheetah.mvp.AbsPresenter;
import com.joey.rxble.RxBle;
import com.joey.rxble.RxBleOperator;
import com.polidea.rxandroidble2.scan.ScanRecord;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class BleScanPresenter extends AbsPresenter<IBleScanView>{
    private RxBleOperator mOperator;
    private boolean scanning;
    private List<ScanResult> datas;
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
        if (scanning) {
            mOperator.stopScan();
            scanning = false;
            if (isValid()) mView.showScan();
        } else {
            scanning = true;
            mOperator.scan()
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> { if (isValid()) mView.showStopScan();})
                    .doOnError(throwable -> {if (isValid()) mView.showScan();})
                    .subscribe(this::result,
                            throwable -> {if (isValid()) mView.toast("scan failed!");});
        }

    }

    private boolean diff(ScanResult result) {
        if (datas == null || datas.isEmpty()) return true;
        return true;
    }

    private void result(ScanResult result) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        for (int i = 0; i < datas.size(); i++) {
            if (TextUtils.equals(datas.get(i).getBleDevice().getMacAddress(),
                    result.getBleDevice().getMacAddress())) {
                datas.set(i, result);
                break;
            } else {
                datas.add(result);
            }
        }
        if (datas.size() == 0) {
            datas.add(result);
        }
        if (isValid()) mView.refresh(datas);
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
