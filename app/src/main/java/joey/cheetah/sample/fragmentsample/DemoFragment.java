package joey.cheetah.sample.fragmentsample;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cheetah.core.mvp.AbsFragment;
import cheetah.core.mvp.AbsPresenter;
import joey.cheetah.R;

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
public class DemoFragment extends AbsFragment implements IDemoFragmentView{
    @BindView(R.id.tv_show)
    TextView mTvShow;
    private DemoFragmentPresenter mPresenter;

    @Override
    protected void initArguments(Bundle arguments) {
        mPresenter.setShow(arguments.getString("show"));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected AbsPresenter initPresenter() {
        mPresenter = new DemoFragmentPresenter(this);
        return mPresenter;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_demo;
    }

    @Override
    public void show(String text) {
        mTvShow.setText(text);
    }
}
