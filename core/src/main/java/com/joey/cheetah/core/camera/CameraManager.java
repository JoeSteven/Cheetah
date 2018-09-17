package com.joey.cheetah.core.camera;

/**
 * 相机管理操作类
 *
 * @author rain
 * @date 2018/09/04
 */
public class CameraManager {

    private CameraHandle mCameraHandle;
    private volatile static CameraManager instance;

    public static CameraManager getInstance() {
        if (instance == null) {
            synchronized (CameraManager.class) {
                if (instance == null) {
                    instance = new CameraManager();
                }
            }
        }

        return instance;
    }

    /**
     * 设置具体Camera的实现
     * @param cameraHandle Camera1Preview1、CameraPreview2
     */
    public void setCameraHandle(CameraHandle cameraHandle) {
        mCameraHandle = cameraHandle;
    }

    /**
     * 打开相机
     */
    public void startCamera() {
        mCameraHandle.openCamera();
    }

    /**
     * 关闭相机
     */
    public void stopCamera() {
        mCameraHandle.stopCamera();
    }

    /**
     * 切换前后置相机
     */
    public void switchCamera() {
        mCameraHandle.switchCamera();
    }

    /**
     * 是否支持闪光灯
     * @return true-支持、false-不支持
     */
    public boolean isFlashValid() {
        return mCameraHandle.isFlashValid();
    }

    /**
     * 设置摄像头ID
     * @param cameraId
     */
    public void setCameraId(@CameraContant.CameraID int cameraId) {
        mCameraHandle.setCameraId(cameraId);
    }

    /**
     * 设置拍照数据回调
     * @param callback
     */
    public void setCameraCapture(CameraHandle.CaptureCameraCallback callback) {
        mCameraHandle.takePicture(callback);
    }

    /**
     * 设置相机预览数据回调
     * @param callback
     */
    public void setCameraPreview(CameraHandle.PreviewCallback callback) {
        mCameraHandle.onPreview(callback);
    }

    /**
     * 设置预览的图像的宽高尺寸
     * @param previewW 预览的宽
     * @param previewH 预览的高
     */
    public void setCameraPreviewWH(int previewW,int previewH) {
        mCameraHandle.setPreviewCameraWH(previewW, previewH);
    }
}
