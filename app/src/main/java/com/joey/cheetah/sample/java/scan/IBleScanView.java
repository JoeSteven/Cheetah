package com.joey.cheetah.sample.java.scan;

import com.joey.cheetah.mvp.IView;
import com.polidea.rxandroidble2.scan.ScanResult;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
interface IBleScanView extends IView{
    void refresh(ScanResult result);
}
