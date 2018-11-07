package com.joey.cheetah.core.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.hardware.Camera.Size;

import com.joey.cheetah.core.global.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CameraConfiguration {

    /**
     * 设置Camera1的相关属性
     *
     * @param camera   相机实例
     * @param cameraId 相机Id编号
     * @param previewW 预览的宽
     * @param previewH 预览的高
     */
    public void setCameraParameters(Camera camera, int cameraId, int previewW, int previewH) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            Size localSize = getOptimalPreviewSize(
                    parameters.getSupportedPreviewSizes(), previewW, previewH);
            if (localSize != null) {
                parameters.setPreviewSize(localSize.width, localSize.height);
            }

            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setPictureFormat(ImageFormat.JPEG);

            camera.setDisplayOrientation(getDisplayOrientation(cameraId));
            camera.setParameters(parameters);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Camera2的相关属性
     *
     * @param view          当前承载预览的view
     * @param cameraManager Camera2的Manager
     * @param cameraId      摄像头Id
     * @param previewW      预览的宽
     * @param previewH      预览的高
     * @return 适合预览的尺寸
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public android.util.Size setCameraParameters(TextureView view, CameraManager cameraManager,
                                                 String cameraId, int previewW, int previewH) {

        android.util.Size mPreviewSize = null;

        try {
            //获得某个摄像头的特征，支持的参数
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            //对Camera2支持的程度
            int level = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            //支持的STREAM CONFIGURATION
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics
                    .SCALER_STREAM_CONFIGURATION_MAP);
            //摄像头支持的预览Size数组
            mPreviewSize = getOptimalPreviewSize(map.getOutputSizes(SurfaceTexture.class),
                    previewW, previewH);

            if (Configuration.ORIENTATION_LANDSCAPE == Global.context().getResources()
                    .getConfiguration().orientation) {
                configureTransform(view, view.getWidth(), view.getHeight(), previewW, previewH);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return mPreviewSize;
    }

    /**
     * 翻转变化
     *
     * @param view       当前承载预览的view
     * @param viewWidth  预览view的宽
     * @param viewHeight 预览view的高
     * @param previewW   预览图像的宽
     * @param previewH   预览图像的高
     */
    private void configureTransform(TextureView view, int viewWidth, int viewHeight,
                                    int previewW, int previewH) {
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewH, previewW);
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
        matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
        float scale = Math.max((float) viewHeight / previewH, (float) viewWidth / previewW);
        matrix.postScale(scale, scale, centerX, centerY);
        matrix.postRotate(270, centerX, centerY);

        view.setTransform(matrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private android.util.Size getOptimalPreviewSize(android.util.Size[] sizes, int w, int h) {
        List<android.util.Size> collectorSizes = new ArrayList<>();
        android.util.Size optimalSize = null;

        for (android.util.Size option : sizes) {
            if (w > h) {
                if (option.getWidth() > w && option.getHeight() > h) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getHeight() > w && option.getWidth() > h) {
                    collectorSizes.add(option);
                }
            }
        }

        Collections.sort(collectorSizes, (s1, s2) ->
                Long.signum(s1.getWidth() * s1.getHeight() - s2.getWidth() * s2.getHeight()));

        optimalSize = collectorSizes.get(0);
        return optimalSize;
    }


    /**
     * Camera1获取最接近预览分辨率
     *
     * @param localList 预览分辨率的数组
     * @param w         预览的宽
     * @param h         预览的高
     */
    private Size getOptimalPreviewSize(List<Size> localList, int w, int h) {
        Size optimalSize = null;

        List<Size> localArrayList = new ArrayList<Size>();
        Iterator<Size> localIterator = localList.iterator();
        while (localIterator.hasNext()) {
            Size localSize = localIterator.next();
            if (localSize.width > localSize.height) {
                localArrayList.add(localSize);
            }
        }
        Collections.sort(localArrayList, new PreviewComparator(w, h));
        optimalSize = localArrayList.get(0);

        return optimalSize;
    }

    /**
     * 比较器
     */
    private class PreviewComparator implements Comparator<Size> {
        int w, h;

        PreviewComparator(int w, int h) {
            this.w = w;
            this.h = h;
        }

        @Override
        public int compare(Size paramSize1, Size paramSize2) {
            return Math.abs(paramSize1.width * paramSize1.height - this.w * this.h)
                    - Math.abs(paramSize2.width * paramSize2.height - this.w * this.h);
        }

    }

    /**
     * 根据摄像头Id来设置屏幕旋转角度
     *
     * @param caremaId 摄像头Id
     * @return 旋转的角度
     */
    private int getDisplayOrientation(int caremaId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(caremaId, info);
        WindowManager wm = (WindowManager) Global.context().getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }

        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}
