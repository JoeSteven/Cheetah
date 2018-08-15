package com.joey.cheetah.sample.java.connect.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
public class BleCharacteristicViewBinder extends AbsItemViewBinder<BluetoothGattCharacteristic, BleCharacteristicViewBinder.BleCharacteristicViewHolder> {
    @Override
    protected int layout() {
        return R.layout.item_connect_characteristic;
    }

    @Override
    protected BleCharacteristicViewHolder createViewHolder(View itemView) {
        return new BleCharacteristicViewHolder(itemView);
    }

    @Override
    protected void onBind(BleCharacteristicViewHolder holder, BluetoothGattCharacteristic item) {
        holder.tvUUID.setText(String.format(Locale.getDefault(), "Characteristic: %s", item.getUuid()));
        holder.tvProperties.setText(String.format(Locale.getDefault(), "property: %s", describeProperties(item)));
    }

    private String describeProperties(BluetoothGattCharacteristic characteristic) {
        List<String> properties = new ArrayList<>();
        if (RxBle.isCharacteristicReadable(characteristic)) properties.add("Read");
        if (RxBle.isCharacteristicWritable(characteristic)) properties.add("Write");
        if (RxBle.isCharacteristicNotifiable(characteristic)) properties.add("Notify");
        if (RxBle.isCharacteristicIndicatable(characteristic)) properties.add("Indicate");
        return TextUtils.join(" ", properties);
    }

    static class BleCharacteristicViewHolder extends AbsViewHolder<BluetoothGattCharacteristic> {
        TextView tvUUID;
        TextView tvProperties;
        public BleCharacteristicViewHolder(View itemView) {
            super(itemView);
            tvUUID = findViewById(R.id.tv_uuid);
            tvProperties = findViewById(R.id.tv_permission);
        }
    }
}
