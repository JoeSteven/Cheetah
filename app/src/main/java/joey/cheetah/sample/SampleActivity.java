package joey.cheetah.sample;


import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import com.joey.cheetah.core.mvp.AbsActivity;
import com.joey.cheetah.core.mvp.auto.Presenter;
import com.joey.cheetah.core.utils.Jumper;
import joey.cheetah.sample.apisample.WeatherActivity;
import joey.cheetah.sample.fragmentsample.FragmentDemoActivity;
import joey.cheetah.sample.imagesample.ImageActivity;
import joey.com.joey.cheetah.R;

public class SampleActivity extends AbsActivity implements ISampleView {

    @Presenter
    private SamplePresenter mPresenter;

    @BindView(R.id.tv_event)
    TextView tvEvent;

    @Override
    protected int initLayout() {
        return R.layout.activity_sample;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.bt_request_permission)
    public void requestPermission() {
        mPresenter.requestBle();
    }

    @OnClick(R.id.bt_go_frag)
    public void goToFragment() {
        Jumper.make(this, FragmentDemoActivity.class).jump();
    }

    @OnClick(R.id.bt_go_image)
    public void goToImage() {
        Jumper.make(this, ImageActivity.class).jump();
    }

    @OnClick(R.id.bt_go_weather)
    public void geToWeather() {
        Jumper.make(this, WeatherActivity.class).jump();
    }

    @OnClick(R.id.bt_go_ble)
    public void geToBle() {
//        Jumper.make(this, BleDemoActivity.class).jump();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void showEvent(String city) {
        tvEvent.setText("received event:" + city);
    }
}
