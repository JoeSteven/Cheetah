package com.joey.cheetah.core.camera;

public interface CameraHandle {
    /**
     * 打开相机
     */
    void openCamera();

    /**
     * 开启相机预览
     */
    void startPreview();

    /**
     * 销毁相机对象
     */
    void stopCamera();

    /**
     * 停止相机预览
     */
    void stopPreview();

    /**
     * 切换前后置摄像头
     */
    void switchCamera();

    /**
     * 闪光灯是否可用
     * @return 是否可用的状态值
     */
    boolean isFlashValid();

    /**
     * 设置预览图像的宽高
     */
    void setPreviewCameraWH(int width,int height);

    /**
     * 设置前置/后置摄像头
     * @param cameraId 前后置摄像头的Id编号
     */
    void setCameraId(int cameraId);

    /**
     * 拍照
     * @param captureCameraCallback 拍照回调
     */
    void takePicture(CaptureCameraCallback captureCameraCallback);

    /**
     * 预览
     * @param previewCallback 预览回调
     */
    void onPreview(PreviewCallback previewCallback);

    interface PreviewCallback {
        void onPreviewFrame(byte[] data,int cameraId,int width,int height);
    }

    interface CaptureCameraCallback {
        void onCapture(byte[] data,int width,int height);
    }
}
