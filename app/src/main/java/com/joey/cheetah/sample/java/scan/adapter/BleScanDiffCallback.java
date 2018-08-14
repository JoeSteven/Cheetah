package com.joey.cheetah.sample.java.scan.adapter;

import android.text.TextUtils;

import com.joey.cheetah.core.list.DiffCallback;
import com.polidea.rxandroidble2.scan.ScanResult;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class BleScanDiffCallback extends DiffCallback{
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ScanResult oldItem = (ScanResult) oldItems.get(oldItemPosition);
        ScanResult newItem = (ScanResult) newItems.get(newItemPosition);
        return TextUtils.equals(newItem.getBleDevice().getMacAddress(),
                oldItem.getBleDevice().getMacAddress());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
