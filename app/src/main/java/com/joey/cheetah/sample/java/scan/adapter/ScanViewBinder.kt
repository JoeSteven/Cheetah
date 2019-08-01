package com.joey.cheetah.sample.java.scan.adapter

import com.joey.cheetah.core.list.AbsItemViewBinder
import com.joey.cheetah.core.list.AbsViewHolder
import com.joey.cheetah.sample.R
import com.polidea.rxandroidble2.scan.ScanResult
import kotlinx.android.synthetic.main.item_scan.*
import java.util.*

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
class ScanViewBinder : AbsItemViewBinder<ScanResult>() {
    override fun layout(): Int {
        return R.layout.item_scan
    }


    override fun onBind(holder: AbsViewHolder, item: ScanResult) {
        val bleDevice = item.bleDevice
        holder.scan_line1.text = String.format(Locale.getDefault(), "%s (%s)", bleDevice.macAddress, bleDevice.name)
        holder.scan_line2.text = String.format(Locale.getDefault(), "RSSI: %d", item.rssi)
        holder.scan_line3.text = String.format(Locale.getDefault(), "Type: %d", item.bleDevice.bluetoothDevice.type)
    }
}
