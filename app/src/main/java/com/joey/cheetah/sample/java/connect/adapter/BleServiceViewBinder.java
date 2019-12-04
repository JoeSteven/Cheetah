package com.joey.cheetah.sample.java.connect.adapter;

import android.bluetooth.BluetoothGattService;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.joey.cheetah.core.list.AbsItemViewBinder;
import com.joey.cheetah.core.list.AbsViewHolder;
import com.joey.cheetah.sample.R;

import java.util.Locale;

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
public class BleServiceViewBinder extends AbsItemViewBinder< BluetoothGattService> {
    @Override
    protected int layout() {
        return R.layout.item_connect_service;
    }

    @Override
    protected void onBind(@NonNull AbsViewHolder holder, @NonNull BluetoothGattService item) {
        ((TextView)holder.findViewById(R.id.tv_service)).setText(String.format(Locale.getDefault(), "Service: %s", item.getUuid()));
    }
}
