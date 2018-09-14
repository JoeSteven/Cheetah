package com.joey.cheetah.core.camera;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public void setCameraHandle(CameraHandle cameraHandle) {
        mCameraHandle = cameraHandle;

//        try {
//            Class clazz = Class.forName("Camera");
//
//            clazz.getMethod()
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


//        try {
//            Class clazz = Class.forName("com.joey.cheetah.core.camera.Camera1Preview");
//            clazz.newInstance();
//            Constructor constructor = clazz.getConstructor();
//
//            Object object = constructor.newInstance();
//            Method method  = clazz.getMethod("setPreviewCameraWH",Integer.class,Integer.class);
//            method.invoke(object,640,480);
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    public void startCamera() {
        mCameraHandle.openCamera();
    }

    public void stopCamera() {
        mCameraHandle.stopCamera();
    }

    public void switchCamera() {
        mCameraHandle.switchCamera();
    }

    public void setCameraId(@CameraContant.CameraID int cameraId) {
        mCameraHandle.setCameraId(cameraId);
    }

    public void setCameraCapture(CameraHandle.CaptureCameraCallback callback) {
        mCameraHandle.takePicture(callback);
    }

    public void setCameraPreview(CameraHandle.PreviewCallback callback) {
        mCameraHandle.onPreview(callback);
    }
}
