package com.joey.cheetah.sample.java.connect.adapter;

import android.bluetooth.BluetoothGattDescriptor;
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
public class BleDescriptorViewBinder extends AbsItemViewBinder<BluetoothGattDescriptor, BleDescriptorViewBinder.BleDescriptorViewHolder> {
    @Override
    protected int layout() {
        return R.layout.item_connect_descrptor;
    }

    @Override
    protected BleDescriptorViewHolder createViewHolder(View itemView) {
        return new BleDescriptorViewHolder(itemView);
    }

    @Override
    protected void onBind(BleDescriptorViewHolder holder, BluetoothGattDescriptor item) {
        holder.tvUUID.setText(String.format(Locale.getDefault(), "descriptor: %s", item.getUuid()));
        holder.tvProperties.setText(String.format(Locale.getDefault(), "permission: %s", item.getPermissions()));
    }


    static class BleDescriptorViewHolder extends AbsViewHolder<BluetoothGattDescriptor> {
        TextView tvUUID;
        TextView tvProperties;
        public BleDescriptorViewHolder(View itemView) {
            super(itemView);
            tvUUID = findViewById(R.id.tv_uuid);
            tvProperties = findViewById(R.id.tv_permission);
        }
    }
}
