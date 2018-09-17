package com.joey.cheetah.core.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.joey.cheetah.core.global.Global;

import java.io.IOException;

/**
 * Camera1视频预览类
 *
 * @author rain
 * @date 2018/09/04
 */
public class Camera1Preview extends SurfaceView implements SurfaceHolder.Callback,
        CameraHandle,Camera.PreviewCallback {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;

    /**
     * 默认前置摄像头
     */
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * 效果最佳默认尺寸(640x480)
     */
    private int mPreviewWidth = CameraConstant.PREVIEWW, mPreviewHeight = CameraConstant.PREVIEWH;

    /**
     * 预览byte数据流
     */
    private byte[] mPreBuffer;

    private CameraConfiguration mConfiguration;

    private ToneGenerator tone;

    private PreviewCallback mPreviewCallback;

    public Camera1Preview(Context context) {
        this(context, null);
    }

    public Camera1Preview(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void openCamera() {
        CameraOpenThread openThread = new CameraOpenThread("CameraOpenThread");

        synchronized (openThread) {
            openThread.openCamera();
        }
    }

    @Override
    public void startPreview() {
        if (mConfiguration != null) {
            mConfiguration.setCameraParameters(mCamera, mCameraId, mPreviewWidth, mPreviewHeight);
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.addCallbackBuffer(mPreBuffer);
                mCamera.setPreviewCallbackWithBuffer(this);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopCamera() {
        if (mCamera != null) {
            stopPreview();

            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.stopPreview();
        }
    }

    @Override
    public void switchCamera() {
        stopCamera();

        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        openCamera();
        startPreview();
    }

    @Override
    public boolean isFlashValid() {
        return mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK && mCamera != null
                && Global.context().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void setPreviewCameraWH(int width, int height) {
        mPreviewWidth = width;
        mPreviewHeight = height;

        mPreBuffer = new byte[width * height * 3 / 2];
    }

    @Override
    public void setCameraId(int cameraId) {
        mCameraId = cameraId;
    }

    @Override
    public void takePicture(CaptureCameraCallback callback) {
        mCamera.autoFocus((flag, camera1) -> camera1.takePicture(() -> {
            if (tone == null) {
                //发出提示用户的声音
                tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
            }
            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
        }, null, (data, camera11) -> {
            Camera.Size size = camera11.getParameters().getPreviewSize();
            int width = size.width;
            int height = size.height;
            callback.onCapture(data,width,height);

            mCamera.startPreview();
        }));
    }

    @Override
    public void onPreview(PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mConfiguration == null) {
            mConfiguration = new CameraConfiguration();
        }

        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        stopPreview();
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (mPreBuffer == null) {
            mPreBuffer = new byte[mPreviewWidth * mPreviewHeight * 3 / 2];
        }

        mCamera.addCallbackBuffer(mPreBuffer);

        Camera.Size size = camera.getParameters().getPreviewSize();
        int width = size.width;
        int height = size.height;

        if (mPreviewCallback != null) {
            mPreviewCallback.onPreviewFrame(data,mCameraId,width,height);
        }
    }

    private class CameraOpenThread extends HandlerThread {

        private Handler mHandler;

        CameraOpenThread(String name) {
            super(name);
            start();
            mHandler = new Handler(getLooper());
        }

        private void openCamera() {
            mHandler.post(() -> {
                if (mCamera == null) {
                    mCamera = Camera.open(mCameraId);
                    synchronized (CameraOpenThread.this) {
                        notify();
                    }
                }
            });

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
