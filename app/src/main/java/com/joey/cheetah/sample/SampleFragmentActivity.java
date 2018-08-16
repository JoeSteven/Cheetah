package com.joey.cheetah.sample;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.joey.cheetah.mvp.AbsPresenter;
import com.joey.cheetah.sample.kt.IGankView;

/**
 * Description:
 * author:Joey
 * date:2018/8/16
 */
public class GankPresenter extends AbsPresenter<IGankView>{

    public GankPresenter(IGankView view) {
        super(view);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        bus().subscribe(LoginEvent.class, t -> mView.login());
    }

    @Override
    public void onSaveData(Bundle outState) {

    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {

    }
}
