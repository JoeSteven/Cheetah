package com.joey.cheetah.sample.java.connect.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.joey.cheetah.core.list.AbsItemViewBinder;
import com.joey.cheetah.core.list.AbsViewHolder;
import com.joey.cheetah.sample.R;
import com.joey.rxble.RxBle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
public class BleCharacteristicViewBinder extends AbsItemViewBinder<BluetoothGattCharacteristic> {
    @Override
    protected int layout() {
        return R.layout.item_connect_characteristic;
    }

    @Override
    protected void onBind(@NonNull AbsViewHolder holder, @NonNull BluetoothGattCharacteristic item) {
        ((TextView)holder.findViewById(R.id.tv_uuid)).setText(String.format(Locale.getDefault(), "Characteristic: %s", item.getUuid()));
        ((TextView)holder.findViewById(R.id.tv_permission)).setText(String.format(Locale.getDefault(), "property: %s", describeProperties(item)));
    }

    private String describeProperties(BluetoothGattCharacteristic characteristic) {
        List<String> properties = new ArrayList<>();
        if (RxBle.isCharacteristicReadable(characteristic)) properties.add("Read");
        if (RxBle.isCharacteristicWritable(characteristic)) properties.add("Write");
        if (RxBle.isCharacteristicNotifiable(characteristic)) properties.add("Notify");
        if (RxBle.isCharacteristicIndicatable(characteristic)) properties.add("Indicate");
        return TextUtils.join(" ", properties);
    }
}
