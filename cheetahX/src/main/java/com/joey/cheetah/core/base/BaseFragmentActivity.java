package com.joey.cheetah.core.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;


/**
 * description -
 * <p/>
 * author - Joe.
 * create on 16/7/18.
 * change
 * change on .
 */
public abstract class BaseFragmentActivity<T extends BasePresenter> extends BaseActivity<T> {
    protected FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        //避免被系统回收后发生重复创建
        if (savedInstanceState == null) {
            createFragment();
        } else {
            restoreFragment(savedInstanceState);
        }
    }

    /**
     * 创建Fragment
     */
    protected abstract void createFragment();

    /**
     * 恢复Fragment
     */
    protected abstract void restoreFragment(Bundle savedInstanceState);

    @SuppressWarnings("unchecked")
    protected <E extends BaseFragment> E findFragment(String tag){
        return (E) mFragmentManager.findFragmentByTag(tag);
    }
}
