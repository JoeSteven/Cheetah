package com.joey.cheetah.sample.ble;

import android.support.annotation.NonNull;

import com.joey.cheetah.core.global.Global;
import com.joey.cheetah.core.permission.PermissionListener;
import com.joey.cheetah.core.permission.PermissionUtil;
import com.joey.rxble.RxBle;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class BlePermissionRequester implements RxBle.PermissionRequester {
    @Override
    public void request(RxBle.PermissionListener listener, String... permissions) {
        if (Global.topActivity() == null) listener.onDenied();
        PermissionUtil.requestPermission(Global.topActivity(), new PermissionListener() {

                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        listener.onGranted();
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {
                        listener.onDenied();
                    }
                },
                permissions);
    }

    @Override
    public boolean hasPermission(String... permissions) {
        return PermissionUtil.hasPermission(Global.topActivity(), permissions);
    }
}
