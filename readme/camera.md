# 相机模块

### 概述
该模块兼容Camera1、Camera2 API，调用统一方法对其进行操作

### 使用方法
1.AndroidManifest.xml中进行相机权限声明

```
 <uses-permission android:name="android.permission.CAMERA"/>
 <uses-feature android:name="android.hardware.camera"/>
 <uses-feature android:name="android.hardware.camera.autofocus"/>
 <uses-feature android:name="android.hardware.camera.flash"/>
    
```

- Android6.0以下即在AndroidManifest.xml中权限声明即可，在6.0以上的系统需进行相机动态权限的声明(相机权限为危险权限)

2.实例化CameraPreview和CameraManager  

```
     Camera1Preview mCameraPreview = Camera1Preview(this)

     WindowManager windowManager = getSystemService(Context.WINDOW_SERVICE)
     windowManager.defaultDisplay.getMetrics(DisplayMetrics())
     int screenWidth = dm.widthPixels
     int screenHight = dm.heightPixels

     //设置屏幕比例与预览尺寸保持一致，此处设置的640x480的尺寸
     mPreviewW = screenWidth
     mPreviewH = (mPreviewW * 640 / 480)
     FrameLayout.LayoutParams params = FrameLayout.LayoutParams(mPreviewW,mPreviewH)
     mCameraPreview.layoutParams = params

     fl_camera.addView(mCameraPreview)
     CameraManager mCameraManager = CameraManager.getInstance()
     //设置相机具体的实现
     mCameraManager.setCameraHandle(mCameraPreview)
     //设置相机ID
     mCameraManager.setCameraId(CameraContant.CAMERAID_BACK)
     //设置预览尺寸
     mCameraManager.setCameraPreviewWH(640,480)  
```  

- cameraPreview尺寸需和预览尺寸保持一致，否则会导致预览图像拉伸变形
- cameraPreview可选择Camera1Preview或是Camera2Preview,其对应的是Camera1、Camera2API

3.实现预览

```
mCameraManager.startCamera()

```

4.停止预览

```
mCameraManager.stopCamera()
```

#### CameraManager相机操作类
1.设置相机的具体实现

```
    /**
     * 设置具体Camera的实现
     * @param cameraHandle Camera1Preview1、CameraPreview2
     */
    public void setCameraHandle(CameraHandle cameraHandle) {
        
    }
```
2.相机操作方法
	
```
	/**
     * 打开相机
     */
    public void startCamera() {
        
    }

    /**
     * 关闭相机
     */
    public void stopCamera() {
        
    }

    /**
     * 切换前后置相机
     */
    public void switchCamera() {
        
    }

    /**
     * 是否支持闪光灯
     * @return true-支持、false-不支持
     */
    public boolean isFlashValid() {
        
    }

    /**
     * 设置摄像头ID
     * @param cameraId(CameraContant.CAMERAID_FRONT,CameraContant.CAMERAID_BACK)
     */
    public void setCameraId(@CameraContant.CameraID int cameraId) {
        
    }

    /**
     * 设置拍照数据回调，默认数据格式为jpg
     * @param callback (byte[] data,int width,int height)
     */
    public void setCameraCapture(CameraHandle.CaptureCameraCallback callback) {
        
    }

    /**
     * 设置相机预览数据回调，默认数据格式为NV21
     * @param callback (byte[] data,int cameraId,int width,int height)
     */
    public void setCameraPreview(CameraHandle.PreviewCallback callback) {
        
    }
    
    /**
     * 设置预览的图像的宽高尺寸
     * @param previewW 预览的宽
     * @param previewH 预览的高
     */
    public void setCameraPreviewWH(int previewW,int previewH) {

    }
``` 
