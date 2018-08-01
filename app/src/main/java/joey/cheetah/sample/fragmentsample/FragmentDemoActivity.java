package joey.cheetah.sample.fragmentsample;


import android.os.Bundle;

import butterknife.OnClick;
import cheetah.core.mvp.AbsFragmentActivity;
import cheetah.core.mvp.AbsPresenter;
import cheetah.core.utils.BundleMaker;
import joey.cheetah.R;

public class FragmentDemoActivity extends AbsFragmentActivity {

    private DemoFragment fragmentOne;
    private DemoFragment fragmentTWo;

    @Override
    protected int initLayout() {
        return R.layout.activity_fragment_demo;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected AbsPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    private int i = 1;
    @OnClick(R.id.bt_switch)
    public void switchFg() {
        if (i%2 == 0) {
            switchFragment(fragmentTWo, R.id.fl_content, "fg_two");
        } else {
            switchFragment(fragmentOne, R.id.fl_content, "fg_one");
        }
        i++;
    }

    @Override
    protected void createFragment() {
        fragmentOne = new DemoFragment();
        fragmentOne.setArguments(BundleMaker.put("show","fragment one!").make());
        fragmentTWo = new DemoFragment();
        fragmentTWo.setArguments(BundleMaker.put("show", "fragment two!").make());
    }

    @Override
    protected void restoreFragment(Bundle savedInstanceState) {
        fragmentOne = (DemoFragment) fragmentManager().findFragmentByTag("fg_one");
        fragmentTWo = (DemoFragment) fragmentManager().findFragmentByTag("fg_two");
    }

    @Override
    protected void attachFragment() {
        addFragment(fragmentOne, R.id.fl_content, "fg_one");
        i++;
    }
}
