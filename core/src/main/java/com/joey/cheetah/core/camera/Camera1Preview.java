package com.joey.cheetah.core.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import androidx.annotation.RequiresApi;

import com.joey.cheetah.core.global.Global;
import com.joey.cheetah.core.utils.CLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Camera1视频预览类
 *
 * @author rain
 * @date 2018/09/04
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera1Preview extends SurfaceView implements SurfaceHolder.Callback,
        CameraHandle,Camera.PreviewCallback {
    private static int START_PREVIEW = 0;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private List<Integer> pendingTask = new ArrayList<>();

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

    private CameraOpenThread mCameraThread;

    public Camera1Preview(Context context) {
        this(context, null);
    }

    public Camera1Preview(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void openCamera() {
        mCameraThread = new CameraOpenThread("CameraOpenThread");

        synchronized (mCameraThread) {
            mCameraThread.openCamera();
        }
    }

    @Override
    public void startPreview() {
        if (mCameraThread != null) {
            mCameraThread.mHandler.post(() -> {
                CLog.d("camera1", "try to start preview" + Thread.currentThread());
                if (mCamera == null) {
                    pendingTask.add(START_PREVIEW);
                    return;
                }

                if (mConfiguration != null) {
                    mConfiguration.setCameraParameters(mCamera, mCameraId, mPreviewWidth, mPreviewHeight);
                    try {

                        mCamera.setPreviewDisplay(mSurfaceHolder);
                        mCamera.addCallbackBuffer(mPreBuffer);
                        mCamera.setPreviewCallbackWithBuffer(Camera1Preview.this);
                        mCamera.startPreview();
                        CLog.d("camera1", "start preview success"+ Thread.currentThread());
                    } catch (IOException e) {
                        e.printStackTrace();
                        CLog.d("camera1", "start preview error:" + e.toString());
                    }
                }
            });
        }

    }

    @Override
    public void stopCamera() {
        if (mCameraThread != null) {
            mCameraThread.mHandler.post(() -> {
                if (mCamera != null) {
                    CLog.d("camera1", "stop camera"+ Thread.currentThread());
                    stopPreview();
                    mCamera.release();
                    mCamera = null;
                    try {
                        mCameraThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mCameraThread.quitSafely();
                    }
                    mCameraThread = null;
                }
            });
        }

    }

    @Override
    public void stopPreview() {
        if (mCameraThread != null) {
            mCameraThread.mHandler.post(() -> {
                CLog.d("camera1", "stop preview"+ Thread.currentThread());
                if (mCamera != null) {
                    mCamera.setPreviewCallbackWithBuffer(null);
                    try {
                        mCamera.setPreviewDisplay(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCamera.stopPreview();
                }
            });
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

        mPreBuffer = new byte[width * height * (ImageFormat.getBitsPerPixel(ImageFormat.NV21))/8];
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
        CLog.d("camera1", "on surface created "+ Thread.currentThread());
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        CLog.d("camera1", "on surface changed"+ Thread.currentThread());
        stopPreview();
        startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CLog.d("camera1", "on surface stop"+ Thread.currentThread());
        stopCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (mPreBuffer == null) {
            mPreBuffer = new byte[mPreviewWidth * mPreviewHeight * (ImageFormat.getBitsPerPixel(ImageFormat.NV21))/8];
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
                    CLog.d("camera1", "open camera success"+ Thread.currentThread());
                    checkPendingTask();
                }
            });

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void checkPendingTask() {
        for(int task: pendingTask) {
            CLog.d("camera1", "execute pending task:"+ task);
            if (task == START_PREVIEW) {
                startPreview();
            }
        }
        pendingTask.clear();
    }
}
