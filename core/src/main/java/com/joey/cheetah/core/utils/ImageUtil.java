package com.joey.cheetah.core.utils;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.joey.cheetah.core.camera.CameraContant;
import com.joey.cheetah.core.global.Global;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 图像转换器
 *
 * @author rain
 * @date 2018/09/12
 */
public class ImageUtil {

    public static final int COLOR_FormatI420 = 1;
    public static final int COLOR_FormatNV21 = 2;

    /**
     * @param data   预览图像数据
     * @param format 数据格式
     * @param width  图像宽
     * @param height 图像高
     * @return 处理的bitmnap
     */
    public static Bitmap yuv2Image(byte[] data, int format, int width, int height) {

        Bitmap bitmap = null;

        YuvImage yuvImage = new YuvImage(data, format, width, height, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 90, outputStream);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        bitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size()
                , options);

        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] getDataFromImage(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return bytes;
    }

    /**
     * 旋转图片
     * @param bitmap 原始图片
     * @return 处理过后的图片
     */
    public static Bitmap rotaingImageView(Bitmap bitmap,@CameraContant.CameraID int cameraID) {
        Matrix matrix = new Matrix();

        switch (cameraID) {
            case CameraContant.CAMERAID_FRONT:
                matrix.postScale(-1, 1);
                break;
            case CameraContant.CAMERAID_BACK:
                break;
            default:
                break;
        }

        if (Global.context().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            matrix.postRotate(90);
        }

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap =null;
        }
        return resizedBitmap;
    }

    /**
     *
     * @param path 图片路径
     * @return 旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
