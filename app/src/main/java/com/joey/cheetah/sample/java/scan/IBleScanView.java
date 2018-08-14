package com.joey.cheetah.sample.java.scan;

import com.joey.cheetah.mvp.IView;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.util.List;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
interface IBleScanView extends IView{
    void refresh(List<ScanResult> results);
    void showScan();
    void showStopScan();
}
