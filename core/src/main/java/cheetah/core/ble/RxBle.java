package cheetah.core.ble;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.annotation.NonNull;

import cheetah.core.permission.PermissionListener;
import cheetah.core.permission.PermissionUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Description: An helper to use Ble module
 * author:Joey
 * date:2018/8/3
 */
public class RxBle {
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

    @SuppressLint("MissingPermission")
    public boolean isEnable() {
        return mBtAdapter.isEnabled();
    }


    @SuppressLint("MissingPermission")
    public void enable(Activity activity) {
        if (isEnable()) return;
        PermissionUtil.requestPermission(activity, new PermissionListener() {

                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        if ( mBtAdapter.enable()) {

                        }
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {

                    }
                },
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public abstract class Comsuer<T> implements Observer<T>{
        @Override
        public void onComplete() {}

        @Override
        public void onSubscribe(Disposable d) {}

    }

}
