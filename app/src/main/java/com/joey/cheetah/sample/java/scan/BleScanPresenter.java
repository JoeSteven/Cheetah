package com.joey.cheetah.sample.java.scan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import com.joey.cheetah.sample.java.AbsBlePresenter;
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
public class BleScanPresenter extends AbsBlePresenter<IBleScanView> {
    private boolean scanning;
    private List<ScanResult> datas;
    public BleScanPresenter(IBleScanView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void scan() {
        if (scanning) {
            operator().stopScan();
            scanning = false;
            if (isValid()) mView.showScan();
        } else {
            scanning = true;
            operator().scan()
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> { if (isValid()) mView.showStopScan();})
                    .doOnError(throwable -> {if (isValid()) mView.showScan();})
                    .subscribe(this::result,
                            throwable -> {if (isValid()) mView.toast("scan failed!");});
        }

    }


    @SuppressLint("CheckResult")
    public void open() {
        operator().enable()
                .subscribe(client -> {if (isValid())mView.toast("open bluetooth success!");},
                        throwable -> {if (isValid())mView.toast("open bluetooth failed!");});
    }

    public void close() {
        operator().disable();
        if (isValid()) mView.toast("close blue tooth success!");
    }
    
    @Override
    public void onPause() {
        operator().stopScan();
        if (isValid()) mView.showScan();
    }

    private void result(ScanResult result) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        for (int i = 0; i < datas.size(); i++) {
            if (TextUtils.equals(datas.get(i).getBleDevice().getMacAddress(),
                    result.getBleDevice().getMacAddress())) {
                datas.set(i, result);
                if (isValid()) mView.refresh(datas);
                return;
            }
        }
        datas.add(result);
        if (isValid()) mView.refresh(datas);
    }

    @Override
    public void onSaveData(Bundle outState) {

    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {

    }
}
