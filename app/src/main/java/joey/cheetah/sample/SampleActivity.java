package joey.cheetah.sample;


import butterknife.OnClick;
import cheetah.core.mvp.AbsActivity;
import cheetah.core.mvp.AbsPresenter;
import cheetah.core.utils.Jumper;
import joey.cheetah.R;
import joey.cheetah.sample.fragmentsample.FragmentDemoActivity;
import joey.cheetah.sample.imagesample.ImageActivity;

public class SampleActivity extends AbsActivity implements ISampleView {

    private SamplePresenter mPresenter;

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
