package com.joey.cheetah.sample.java.connect;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;

import com.joey.cheetah.core.media.ble.BleSplitData;
import com.joey.cheetah.core.utils.CLog;
import com.joey.cheetah.core.utils.HexString;
import com.joey.cheetah.sample.java.AbsBlePresenter;
import com.polidea.rxandroidble2.NotificationSetupMode;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDeviceServices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
public class BleConnectPresenter extends AbsBlePresenter<IBleConnectView> {
    private boolean connected;
    private String device;
    private Disposable notifyOrIndicate;

    public BleConnectPresenter(IBleConnectView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    public void connectOrDisconnect(String device) {
        this.device = device;
        if (connected) {
            operator().disconnect();
            if (isValid()) {
                mView.showConnect();
                mView.showConnectList(new ArrayList<>());
            }
            connected = false;
        } else {
            operator().setConnectRetryTimes(3)
                    .setConnectRetryTimes(1000)
                    .connect(device)
                    .doOnSubscribe(disposable -> {
                        if (isValid()) mView.showConnecting();
                    })
                    .flatMapSingle((Function<RxBleConnection, SingleSource<RxBleDeviceServices>>) RxBleConnection::discoverServices)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::connectResult,
                            throwable -> {
                                if (isValid()) {
                                    mView.toast("connect failed:" + throwable.toString());
                                    mView.showConnect();
                                    mView.showConnectList(null);
                                    connected = false;
                                }
                            });
        }
    }

    @SuppressLint("CheckResult")
    public void read(UUID uuid) {
        operator().readCharacteristic(device, uuid)
                .subscribe(bytes -> done("read success:" + HexString.byte2Hex(bytes)),
                        e -> mView.toast("read error:" + e.toString()));
    }

    @SuppressLint("CheckResult")
    public void write(UUID uuid, String s) {
        BleSplitData data = new BleSplitData(HexString.hex2Byte(s), HexString.hex2Byte("0B7C7D7E"));
        Single<byte[]> single = operator().writeCharacteristic(device, uuid, data.getDataList().get(0));
        for (int i = 1; i < data.getDataList().size(); i++) {
            int finalI = i;
            single = single.flatMap((Function<byte[], SingleSource<byte[]>>) bytes -> operator().writeCharacteristic(device, uuid, data.getDataList().get(finalI)));
        }
        single.flatMap((Function<byte[], SingleSource<byte[]>>) bytes -> operator().writeCharacteristic(device, uuid, data.getEndFrame()))
                .subscribe(bytes -> done("write success:" + HexString.byte2Hex(bytes)),
                e -> mView.toast("write error:" + e.toString()));
    }

    public void notification(UUID uuid) {
        disableNotifyOrIndicate();
        notifyOrIndicate = operator().setupNotification(device, uuid, NotificationSetupMode.DEFAULT)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(o -> mView.switchNotifyOrIndicate("Disable Notify", true))
                .flatMap(observable -> observable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> mView.switchNotifyOrIndicate("No notify", false))
                .subscribe(bytes -> done("notify data:" + new String(bytes)),
                        e -> mView.toast("notify error:" + e.toString()));
    }

    public void indicate(UUID uuid) {
        disableNotifyOrIndicate();
        notifyOrIndicate = operator().setUpIndication(device, uuid, NotificationSetupMode.DEFAULT)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(o -> mView.switchNotifyOrIndicate("Disable Indicate", true))
                .flatMap(observable -> observable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> mView.switchNotifyOrIndicate("No Indicate", false))
                .subscribe(bytes -> done("indicate data:" + HexString.byte2Hex(bytes)),
                        e -> mView.toast("indicate error:" + e.toString()));
    }

    @SuppressLint("CheckResult")
    public void readDescriptor(BluetoothGattDescriptor result) {
        operator().readDescriptor(device, result)
                .subscribe(bytes -> done("read success:" + HexString.byte2Hex(bytes)),
                        e -> mView.toast("read error:" + e.toString()));
    }

    @SuppressLint("CheckResult")
    public void writeDescriptor(BluetoothGattDescriptor result, String s) {
        operator().writeDescriptor(device, result, HexString.hex2Byte(s))
                .subscribe(bytes -> done("write success:" + HexString.byte2Hex(bytes)),
                        e -> mView.toast("write error:" + e.toString()));
    }

    public void disableNotifyOrIndicate() {
        if (notifyOrIndicate != null && !notifyOrIndicate.isDisposed()) {
            notifyOrIndicate.dispose();
        }
        mView.switchNotifyOrIndicate("No", false);
    }

    private void done(String message) {
        CLog.d("hahaha", message);
        if (isValid()) {
            mView.showMessage(message);
        }
    }

    private void connectResult(RxBleDeviceServices rxBleDeviceServices) {
        List datas = new ArrayList<>();
        if (rxBleDeviceServices != null) {
            for (BluetoothGattService service : rxBleDeviceServices.getBluetoothGattServices()) {
                datas.add(service);
                for (BluetoothGattCharacteristic c : service.getCharacteristics()) {
                    datas.add(c);
                    datas.addAll(c.getDescriptors());
                }
            }
        }
        if (isValid()) {
            mView.showDisconnect();
            mView.showConnectList(datas);
        }
        connected = true;
    }

    @Override
    public void onSaveData(Bundle outState) {
    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {
    }

}
