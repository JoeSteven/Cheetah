package joey.cheetah.sample;


import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cheetah.core.mvp.AbsActivity;
import cheetah.core.mvp.AbsPresenter;
import cheetah.core.utils.Jumper;
import joey.cheetah.R;
import joey.cheetah.sample.apisample.WeatherActivity;
import joey.cheetah.sample.ble.BleDemoActivity;
import joey.cheetah.sample.fragmentsample.FragmentDemoActivity;
import joey.cheetah.sample.imagesample.ImageActivity;

public class SampleActivity extends AbsActivity implements ISampleView {

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
    protected AbsPresenter initPresenter() {
        mPresenter = new SamplePresenter(this);
        return mPresenter;
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
        Jumper.make(this, BleDemoActivity.class).jump();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void showEvent(String city) {
        tvEvent.setText("received event:" + city);
    }
}
