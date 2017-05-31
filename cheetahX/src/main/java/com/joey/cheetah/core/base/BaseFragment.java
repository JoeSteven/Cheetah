package com.joey.cheetah.core.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.joey.cheetah.core.CheetahApplication;
import com.joey.cheetah.uitls.TUtil;


/**
 * description - fragment 基类 里面的方法大多同BaseActivity类似
 * author - Joe.
 * create on 16/7/12.
 * change
 * change on .
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    protected static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected BaseActivity mActivity;
    protected CheetahApplication mApplication;
    protected View mRootView;
    protected T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSaveFragmentInstance(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) getActivity();
        mApplication = (CheetahApplication) mActivity.getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(), null);
        mPresenter = TUtil.getT(this, 0);
        initArguments();
        initView();
        initListener();
        initPresenter();
        if (mPresenter != null){
            mPresenter.onStart();
        }
        return mRootView;
    }

    /**
     * 获取参数
     */
    protected void initArguments() {

    }

    /**
     * 设置布局
     */
    protected abstract int getLayout();

    /**
     * 初始化UI
     */
    protected abstract void initView();

    /**
     * 初始化presenter
     */
    protected abstract void initPresenter();

    /**
     * 监听放在这里
     */
    protected void initListener() {

    }

    @Override
    public void showToast(String msg) {
        if (mActivity == null) return;
        mActivity.showToast(msg);
    }

    public void showLoading(String msg) {
        if (mActivity == null) return;
        mActivity.showLoading(msg);
    }

    public void doneLoading() {
        if (mActivity == null) return;
        mActivity.doneLoading();
    }

    //处理Fragment被回收时发送的重叠现象
    private void onSaveFragmentInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    /**恢复数据*/
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        initArguments();
    }

    /**
     * findViewById不需要强转
     */
    @SuppressWarnings("unchecked")
    public <E extends View> E findView(int id){
        return (E) mRootView.findViewById(id);
    }

    /**
     * 当前Fragment 类名
     */
    public String getName() {
        return getClass().getSimpleName();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onRemove();
        doneLoading();
    }
}
