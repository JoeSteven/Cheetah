package com.joey.cheetah.mvp;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joey.cheetah.core.ui.CToast;
import com.joey.cheetah.core.utils.ResGetter;
import com.joey.cheetah.mvp.auto.PresenterProvider;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class AbsFragment extends Fragment implements IView {
    protected static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected View mRootView;
    protected Activity mActivity;
    private Unbinder mUnBinder;
    private PresenterProvider mPresenterProvider;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = getActivity();
        mPresenterProvider  = createProvider();
        if (mPresenterProvider != null) {
            mPresenterProvider.create(this);
        }
        initArguments(getArguments());
        initPresenter();
        initView();
        if (savedInstanceState != null && mPresenterProvider != null) {
            mPresenterProvider.onRestore(savedInstanceState);
        } else {
            initData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resource = initLayout();
        mRootView = inflater.inflate(resource, container, false);
        mUnBinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    protected PresenterProvider createProvider() {
        return new PresenterProvider();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRestoreFragmentInstance(savedInstanceState);
    }

    private void onRestoreFragmentInstance(Bundle savedInstanceState) {
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        if (mPresenterProvider != null) {
            mPresenterProvider.onSave(outState);
        }
        super.onSaveInstanceState(outState);
    }

    protected abstract void initArguments(Bundle arguments);

    protected abstract void initView();

    protected void initPresenter(){}

    protected void initData() {}

    protected abstract int initLayout();

    public <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    protected boolean isValid() {
        if (mActivity == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(mActivity.isDestroyed() || mActivity.isFinishing());
        }
        return !mActivity.isFinishing();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    @Override
    public void toast(String msg) {
        CToast.show(msg);
    }

    @Override
    public void toast(int stringRes) {
        CToast.show(ResGetter.string(stringRes));
    }
}
