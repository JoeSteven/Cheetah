package com.joey.cheetah.sample;

import com.joey.cheetah.core.utils.Jumper;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.sample.dbdemo.DbDemoActivity;
import com.joey.cheetah.sample.extension.ExtensionActivity;
import com.joey.cheetah.sample.java.scan.BleScanActivity;
import com.joey.cheetah.sample.kt.CameraActivity;
import com.joey.cheetah.sample.kt.GankActivity;
import com.joey.cheetah.sample.photo.PhotoActivity;
import com.joey.cheetah.sample.udp.UdpActivity;


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
        findViewById(R.id.bt_java).setOnClickListener(v -> Jumper.make(this, BleScanActivity.class).jump());

        findViewById(R.id.bt_kt).setOnClickListener(v -> Jumper.make(this, GankActivity.class).jump());

        findViewById(R.id.bt_extension).setOnClickListener(v -> Jumper.make(this, ExtensionActivity.class).jump());

        findViewById(R.id.bt_database).setOnClickListener(v -> Jumper.make(this, DbDemoActivity.class).jump());

        findViewById(R.id.bt_udp).setOnClickListener(v -> Jumper.make(this, UdpActivity.class).jump());

        findViewById(R.id.bt_photo).setOnClickListener(v -> Jumper.make(this, PhotoActivity.class).jump());
    }

    @Override
    protected void initData() {}
}
