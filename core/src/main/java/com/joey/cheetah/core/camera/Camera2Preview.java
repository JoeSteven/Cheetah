package com.joey.cheetah.core.camera;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.joey.cheetah.core.global.Global;
import com.joey.cheetah.core.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Preview extends TextureView implements TextureView.SurfaceTextureListener,CameraHandle {

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * 摄像头Id
     */
    private String mCameraId = "-1";

    /**
     * 预览图像尺寸
     */
    private Size mPreviewSize;

    /**
     * 预览数据流，用于算法处理
     */
    private ImageReader mImageReader;

    /**
     * 照片数据流
     */
    private ImageReader mPictureReader;

    private CameraCaptureSession mPreviewSession;

    private CameraConfiguration mConfiguration;

    private CameraDevice mCameraDevice;
    private boolean isCameraOpened;
    private CaptureRequest.Builder mPreviewBuilder;

    private HandlerThread mBackgroundThread;
    private Handler mHandler;

    private PreviewCallback mPreviewCallback;
    private CaptureCameraCallback mCaptureCameraCallback;

    /**
     * 效果最佳默认尺寸(640x480)
     */
    private int mPreviewWidth = 640, mPreviewHeight = 480;

    public Camera2Preview(Context context) {
        this(context,null);
    }

    public Camera2Preview(Context context, AttributeSet attrs) {
        super(context, attrs);

        setSurfaceTextureListener(this);
        mConfiguration = new CameraConfiguration();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        stopPreview();
        startPreview();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        stopPreview();

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void openCamera() {
        //获得摄像头的管理器CameraManager
        CameraManager cameraManager = (CameraManager) Global.context().getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }

            if (cameraManager != null) {
                //默认前置摄像头
                if (mCameraId.equals("-1")) {
                    mCameraId = cameraManager.getCameraIdList()[1];
                }

                mPreviewSize = mConfiguration.setCameraParameters(this,cameraManager,mCameraId,mPreviewWidth,mPreviewHeight);
                //打开相机
                cameraManager.openCamera(mCameraId,mCameraDeviceStateCallback,null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startPreview() {

        if (mCameraDevice == null || !isCameraOpened) {
            return;
        }

        startBackgroundThread();
        try {
            setImageReader();
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();
            SurfaceTexture texture = this.getSurfaceTexture();
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface textureSurface = new Surface(texture);
            Surface imageSurface = mImageReader.getSurface();
            Surface picSurface = mPictureReader.getSurface();
            surfaces.add(textureSurface);
            surfaces.add(imageSurface);
            surfaces.add(picSurface);
            mPreviewBuilder.addTarget(textureSurface);
            mPreviewBuilder.addTarget(imageSurface);
            mPreviewBuilder.addTarget(picSurface);

            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mPreviewSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },mHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setImageReader() {
        mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                ImageFormat.YUV_420_888, 10);

        mPictureReader = ImageReader.newInstance(mPreviewSize.getWidth(),mPreviewSize.getHeight(),
                ImageFormat.JPEG,2);

        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                if (mPreviewCallback != null) {
                    byte[] nv21Data = ImageUtil.getDataFromImage(image);
                    mPreviewCallback.onPreviewFrame(nv21Data,Integer.valueOf(mCameraId),mPreviewSize.getWidth(),mPreviewSize.getHeight());
                }
                image.close();
            }
        }, mHandler);

        mPictureReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                if (mCaptureCameraCallback != null) {
                    byte[] jpgData = ImageUtil.getDataFromImage(image);
                    mCaptureCameraCallback.onCapture(jpgData,mPreviewSize.getWidth(),mPreviewSize.getHeight());
                }
                image.close();
            }
        },mHandler);
    }


    private void updatePreview() {
        if (mCameraDevice == null) {
            return;
        }

        try {
            mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
            //成功配置后，便开始进行相机图像的监听
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();

            if (mImageReader != null) {
                mImageReader.close();
                mImageReader = null;
            }

            if (mPictureReader != null) {
                mPictureReader.close();
                mPictureReader = null;
            }

            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }

        stopBackgroundThread();
    }

    @Override
    public void stopPreview() {

    }

    @Override
    public void switchCamera() {
        stopCamera();

        CameraManager cameraManager = (CameraManager) Global.context().getSystemService(Context.CAMERA_SERVICE);

        if (cameraManager != null) {
            try {
                if (mCameraId.equals(cameraManager.getCameraIdList()[1])) {
                    mCameraId = cameraManager.getCameraIdList()[0];
                } else if (mCameraId.equals(cameraManager.getCameraIdList()[0])){
                    mCameraId = cameraManager.getCameraIdList()[1];
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            };
        }

        openCamera();
        startPreview();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFlashValid() {
        return false;
    }

    @Override
    public void setPreviewCameraWH(int width, int height) {
        mPreviewWidth = width;
        mPreviewHeight = height;
    }

    @Override
    public void setCameraId(int cameraId) {
        CameraManager cameraManager = (CameraManager) Global.context().getSystemService(Context.CAMERA_SERVICE);

        if (cameraManager != null) {
            try {
                mCameraId = cameraManager.getCameraIdList()[cameraId];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            };
        }
    }

    @Override
    public void takePicture(CaptureCameraCallback captureCameraCallback) {
        mCaptureCameraCallback = captureCameraCallback;
    }

    @Override
    public void onPreview(PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }

    private void closePreviewSession() {
        if (mPreviewSession != null && mCameraDevice != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice
            .StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            mCameraOpenCloseLock.release();
            isCameraOpened = true;
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
            isCameraOpened = false;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
            isCameraOpened = false;
        }
    };
}
