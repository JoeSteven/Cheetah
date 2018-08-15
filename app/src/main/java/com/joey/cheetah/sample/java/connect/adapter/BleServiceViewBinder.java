package com.joey.cheetah.sample.java.connect.adapter;

import android.bluetooth.BluetoothGattService;
import android.view.View;
import android.widget.TextView;

import com.joey.cheetah.core.list.AbsItemViewBinder;
import com.joey.cheetah.core.list.AbsViewHolder;
import com.joey.cheetah.sample.R;

import java.util.Locale;

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
public class BleServiceViewBinder extends AbsItemViewBinder< BluetoothGattService, BleServiceViewBinder.BleServiceViewHolder> {
    @Override
    protected int layout() {
        return R.layout.item_connect_service;
    }

    @Override
    protected BleServiceViewHolder createViewHolder(View itemView) {
        return new BleServiceViewHolder(itemView);
    }

    @Override
    protected void onBind(BleServiceViewHolder holder, BluetoothGattService item) {
        holder.tvService.setText(String.format(Locale.getDefault(), "Service: %s", item.getUuid()));
    }

    static class BleServiceViewHolder extends AbsViewHolder<BluetoothGattService> {
        TextView tvService;
        public BleServiceViewHolder(View itemView) {
            super(itemView);
            tvService = findViewById(R.id.tv_service);
        }
    }
}
