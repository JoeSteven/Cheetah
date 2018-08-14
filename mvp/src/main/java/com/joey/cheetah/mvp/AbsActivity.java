package com.joey.cheetah.mvp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.joey.cheetah.core.mvp.auto.IPresenterProvider;
import com.joey.cheetah.core.mvp.auto.PresenterProvider;
import com.joey.cheetah.core.ui.CToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class AbsActivity extends AppCompatActivity implements IView {
    private Unbinder mUnBinder;
    private IPresenterProvider mPresenterProvider;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        mUnBinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        initArguments(intent);
        mPresenterProvider = createProvider();
        mPresenterProvider.create(this);
        initPresenter();
        initView();
        if (savedInstanceState != null) {
            mPresenterProvider.onRestore(savedInstanceState);
            afterRestoredData();
        } else {
            initData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPresenterProvider.onSave(outState);
        super.onSaveInstanceState(outState);
    }

    private void initArguments(Intent intent) {

    }

    protected abstract int initLayout();

    protected abstract void initView();

    protected void initPresenter(){}

    protected abstract void initData();

    /**
     * if you want to do some logic after restored data
     */
    protected void afterRestoredData() {

    }

    @Override
    public void toast(int stringRes) {
        CToast.show(stringRes);
    }

    @Override
    public void toast(String msg) {
        CToast.show(msg);
    }

    public boolean isValid() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !(isDestroyed() || isFinishing());
        }
        return !isFinishing();
    }

    /**
     * quit this activity with twice back press
     */
    protected boolean isTwiceQuit() {
        return false;
    }

    private long lastTime = 0;

    @Override
    public void onBackPressed() {
        if (!isTwiceQuit()) {
            finish();
            return;
        }
        long duration = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (duration < 2000) {
            finish();
            return;
        }
        CToast.show(R.string.twice_quit_hint);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    private IPresenterProvider createProvider() {
        return new PresenterProvider();
    }
}
