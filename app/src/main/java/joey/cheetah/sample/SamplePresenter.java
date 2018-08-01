package joey.cheetah.sample;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;

import cheetah.core.mvp.AbsPresenter;
import cheetah.core.permission.PermissionListener;
import cheetah.core.permission.PermissionUtil;
import cheetah.core.utils.Global;
import joey.cheetah.sample.apisample.WeatherEvent;

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
public class SamplePresenter extends AbsPresenter<ISampleView>{
    public SamplePresenter(ISampleView view) {
        super(view);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        bus().subscribe(WeatherEvent.class, weather ->{if (isValid()) mView.showEvent(weather.getCity());});
    }

    @Override
    public void onSaveData(Bundle outState) {

    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {

    }

    public void requestBle() {
        PermissionUtil.requestPermission(Global.context(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                if (isValid()) mView.toast("Permission has granted!");
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                if (isValid()) mView.toast("Permission has denied!");
            }
        }, "android.permission.CAMERA");
    }
}
