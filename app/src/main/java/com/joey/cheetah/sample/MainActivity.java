package com.joey.cheetah.sample;

import android.os.Build;
import android.view.View;

import com.joey.cheetah.core.permission.PermissionUtil;
import com.joey.cheetah.core.utils.CLog;
import com.joey.cheetah.core.utils.Jumper;
import com.joey.cheetah.core.utils.ResGetter;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.sample.dbdemo.DbDemoActivity;
import com.joey.cheetah.sample.extension.ExtensionActivity;
import com.joey.cheetah.sample.face.FaceActivity;
import com.joey.cheetah.sample.java.scan.BleScanActivity;
import com.joey.cheetah.sample.kt.GankActivity;
import com.joey.cheetah.sample.photo.PhotoActivity;
import com.joey.cheetah.sample.udp.UdpActivity;
import com.joey.cheetah.core.media.scan.ScanCodeHelper;


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

        findViewById(R.id.bt_scan).setOnClickListener(v -> scan());

        findViewById(R.id.bt_face).setOnClickListener(v -> Jumper.make(this, FaceActivity.class).jump());

        findViewById(R.id.bt_permission).setOnClickListener(v -> PermissionUtil.gotoSetting(this, ResGetter.stringFormat(R.string.jump_to_setting_hint, "存储")));
    }

    private void scan() {
        ScanCodeHelper.INSTANCE
                .scan(this)
                .subscribe(s -> toast(s),
                        throwable -> toast(throwable.toString()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.ll).post(() -> {
            CLog.d("TestMeasure", "" + findViewById(R.id.ll).getHeight());
            findViewById(R.id.tv_test).setVisibility(View.GONE);
//            findViewById(R.id.tv_test).post(() -> {
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1,View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1,View.MeasureSpec.AT_MOST);
            findViewById(R.id.ll).measure(widthMeasureSpec, heightMeasureSpec);
//            findViewById(R.id.ll).measure(,0);
                CLog.d("TestMeasure", "" + findViewById(R.id.ll).getHeight());
//            });

        });

    }

    @Override
    protected void initData() {}
}
