package com.joey.cheetah.sample;

import android.util.Log;

import com.joey.cheetah.core.permission.PermissionUtil;
import com.joey.cheetah.core.utils.Jumper;
import com.joey.cheetah.core.utils.ResGetter;
import com.joey.cheetah.mvp.AbsActivity;
import com.joey.cheetah.sample.dbdemo.DbDemoActivity;
import com.joey.cheetah.sample.exception.RebootHelper;
import com.joey.cheetah.sample.extension.ExtensionActivity;
import com.joey.cheetah.sample.face.FaceActivity;
import com.joey.cheetah.sample.java.scan.BleScanActivity;
import com.joey.cheetah.sample.kt.CameraActivity;
import com.joey.cheetah.sample.kt.GankActivity;
import com.joey.cheetah.sample.photo.PhotoActivity;
import com.joey.cheetah.sample.udp.UdpActivity;
import com.joey.cheetah.core.media.scan.ScanCodeHelper;
import com.terminus.iotextension.event.PersonInfoEvent;
import com.terminus.iotextension.mqtt.IotFrame;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDataSync;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


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

        findViewById(R.id.bt_photo).setOnClickListener(v -> Jumper.make(this, CameraActivity.class).jump());

        findViewById(R.id.bt_scan).setOnClickListener(v -> scan());

        findViewById(R.id.bt_face).setOnClickListener(v -> Jumper.make(this, FaceActivity.class).jump());

        findViewById(R.id.bt_reboot).setOnClickListener(v -> { throw new IllegalArgumentException("test exception"); });

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
        if (RebootHelper.INSTANCE.isRebooted()) {
            toast("异常重启成功");
        }


    }


    @Override
    protected void initData() {
        workPersonList();
    }

    byte[] array = {-125, 64, 29, -78, -56, -36, 125, 17, -3, 102, -52, 9, 65, -46, -23, -6, -21, 41, -90, 85, -125, -30, 31, -89, 95, -90, 118, 92, -39, -17, 59, -2, 92, -48, 110, -7, 31, -81, 25, -25, 127, 40, -90, -4, 7, -46, 4, 34};
    byte[] array2 = {-125, 64, 29, -78, -56, -36, 125, 17, -3, 102, -52, 9, 65, -46, -23, -6, -21, 41, -90, 85, -125, -30, 31, -89, 95, -90, 118, 92, -39, -17, 59, -2, 79, -34, -98, 36, -85, -107, 29, 2, -23, 89, -35, 107, -111, -41, 112, 82};

    private void workPersonList() {

        InputStream input = new ByteArrayInputStream(array2);
        try {
            TSLIOTDataSync.TSLIOTDispatchPersonListRequest result =
                    TSLIOTDataSync.TSLIOTDispatchPersonListRequest.parseFrom()

            if (result != null) {
                if (result.getListList().size() > 0) {
                    PersonInfoEvent infoEvent = new PersonInfoEvent(result.getDevId(),result.getVersion(),
                            result.getMore(),result.getListList());

                }
            }
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.e("i", e.getMessage());
            }
        }
    }
}
