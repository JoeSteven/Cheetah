package com.joey.cheetah.core.camera;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 相机摄像头ID
 * @author rain
 * @date 2018/09/12
 */
public final class CameraConstant {

    /**
     * 前置摄像头
     */
    public static final int CAMERAID_FRONT = 1;

    /**
     * 后置摄像头
     */
    public static final int CAMERAID_BACK = 0;

    public static final int PREVIEWW = 640;

    public static final int PREVIEWH = 480;

    @IntDef({CAMERAID_FRONT,CAMERAID_BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraID{

    }
}
