package com.joey.cheetah.sample;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.joey.cheetah.core.utils.Jumper;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.mvp.auto.Presenter;
import com.joey.cheetah.sample.java.scan.BleScanActivity;
import com.joey.cheetah.sample.kt.GankActivity;


/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class MainActivity extends AbsActivity{
    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.bt_java).setOnClickListener(v -> {
            Jumper.make(this, BleScanActivity.class).jump();});

        findViewById(R.id.bt_kt).setOnClickListener(v -> {
            Jumper.make(this, GankActivity.class).jump();});
    }

    @Override
    protected void initData() {}
}
