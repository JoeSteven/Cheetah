package com.joey.cheetah.sample.java.connect.adapter;

import android.bluetooth.BluetoothGattDescriptor;
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
public class BleDescriptorViewBinder extends AbsItemViewBinder<BluetoothGattDescriptor> {
    @Override
    protected int layout() {
        return R.layout.item_connect_descrptor;
    }

    @Override
    protected void onBind(@NonNull AbsViewHolder holder, @NonNull BluetoothGattDescriptor item) {
        ((TextView)holder.findViewById(R.id.tv_uuid)).setText(String.format(Locale.getDefault(), "descriptor: %s", item.getUuid()));
        ((TextView)holder.findViewById(R.id.tv_permission)).setText(String.format(Locale.getDefault(), "permission: %s", item.getPermissions()));
    }
}
