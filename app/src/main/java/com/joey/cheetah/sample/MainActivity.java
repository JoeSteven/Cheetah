package com.joey.cheetah.sample;

import com.joey.cheetah.core.utils.Jumper;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.sample.apisample.WeatherActivity;
import com.joey.cheetah.sample.java.scan.BleScanActivity;


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
            Jumper.make(this, WeatherActivity.class).jump();});
    }

    @Override
    protected void initData() {}
}