package joey.cheetah.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;

import cheetah.core.mvp.AbsPresenter;
import cheetah.core.permission.PermissionListener;
import cheetah.core.permission.PermissionUtil;
import cheetah.core.utils.Global;

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
