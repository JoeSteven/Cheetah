package joey.cheetah.sample;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.joey.cheetah.core.mvp.AbsPresenter;
import com.joey.cheetah.core.permission.PermissionListener;
import com.joey.cheetah.core.permission.PermissionUtil;
import com.joey.cheetah.core.utils.Global;
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

    }
}
