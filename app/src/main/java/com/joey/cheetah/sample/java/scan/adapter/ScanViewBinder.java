package com.joey.cheetah.sample.java.scan.adapter;

import android.view.View;
import android.widget.TextView;

import com.joey.cheetah.core.list.AbsItemViewBinder;
import com.joey.cheetah.core.list.AbsViewHolder;
import com.joey.cheetah.sample.R;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.util.Locale;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class ScanViewBinder extends AbsItemViewBinder<ScanResult, ScanViewBinder.ScanViewHolder>{
    @Override
    protected int layout() {
        return R.layout.item_scan;
    }

    @Override
    protected ScanViewHolder createViewHolder(View itemView) {
        return new ScanViewHolder(itemView);
    }

    @Override
    protected void onBind(ScanViewHolder holder, ScanResult item) {
        RxBleDevice bleDevice = item.getBleDevice();
        holder.line1.setText(String.format(Locale.getDefault(), "%s (%s)", bleDevice.getMacAddress(), bleDevice.getName()));
        holder.line2.setText(String.format(Locale.getDefault(), "RSSI: %d", item.getRssi()));
        holder.line3.setText(String.format(Locale.getDefault(), "Type: %d", item.getBleDevice().getBluetoothDevice().getType()));
    }


    static class ScanViewHolder extends AbsViewHolder<ScanResult> {

        TextView line1;
        TextView line2;
        TextView line3;
        public ScanViewHolder(View itemView) {
            super(itemView);
            line1 = findViewById(R.id.scan_line1);
            line2 = findViewById(R.id.scan_line2);
            line3 = findViewById(R.id.scan_line3);
        }
    }
}
