package com.joey.cheetah.core.media;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.WorkerThread;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.concurrent.ExecutionException;

import com.joey.cheetah.core.media.glide.GlideApp;
import com.joey.cheetah.core.media.glide.GlideRequests;
import com.joey.cheetah.core.storage.FileHelper;

/**
 * Description: 上层调用
 * author:Joey
 * date:2018/7/30
 */
public class ImageHelper {
    private static Context initContext;

    public static void init(Context context) {
        if (context == null)
            throw new IllegalArgumentException("context for ImageHelper init can not be null!!!");
        initContext = context;
    }

    public static File getCachePath() {
        return GlideApp.getPhotoCacheDir(context());
    }

    /**
     * start to load an image
     *
     * @param context
     * @return
     */
    public static GlideRequests with(Context context) {
        return GlideApp.with(context);
    }

    /**
     * get Glide
     */
    public static Glide get(Context context) {
        return GlideApp.get(context);
    }


    /**
     * clear all disk caches
     * must call this method in background thread
     */
    @WorkerThread
    public static void clearDiskCaches() {
        if (isOnMainThread())
            throw new IllegalArgumentException("You must call this method on a background thread");
        GlideApp.get(context()).clearDiskCache();
    }

    /**
     * clear all memory caches
     */
    public static void clearMemoryCaches() {
        GlideApp.get(context()).clearMemory();
    }

    public static void trimMemory(int level) {
        GlideApp.get(context()).trimMemory(level);
    }

    /**
     * save image to gallery
     * must call this method in background thead
     */
    @WorkerThread
    public static File saveImageIntoGallery(final String url, final String savePath) {
        if (isOnMainThread())
            throw new IllegalArgumentException("You must call this method on a background thread");
        FutureTarget<File> target = GlideApp.with(context())
                .asFile()
                .load(url)
                .submit();
        File srcFile = null;
        try {
            srcFile = target.get();
            if (FileHelper.copy(srcFile.getAbsolutePath(), savePath)) {
                FileHelper.saveImageToGallery(context(), new File(savePath));
                return new File(savePath);
            } else {
                FileHelper.saveImageToGallery(context(), srcFile);
                return srcFile;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static Context context() {
        if (initContext == null) {
            throw new IllegalArgumentException("You must init initContext");
        }
        return initContext;
    }

}
