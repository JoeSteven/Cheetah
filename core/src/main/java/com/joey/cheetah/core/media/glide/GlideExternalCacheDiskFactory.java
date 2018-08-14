package com.joey.cheetah.core.media.glide;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;

import java.io.File;

/**
 * Description:
 * author:Joey
 * date:2018/7/30
 */
public class GlideExternalCacheDiskFactory extends DiskLruCacheFactory {
    public GlideExternalCacheDiskFactory(Context context) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR,
                DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public GlideExternalCacheDiskFactory(Context context, long diskCacheSize) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public GlideExternalCacheDiskFactory(final Context context, final String diskCachePath,
                                                  final long diskCacheSize) {
        super(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                if (diskCachePath != null) {
                    return new File(diskCachePath);
                }

                File cacheDirectory = context.getExternalCacheDir();
                if (cacheDirectory == null) {
                    return null;
                }

                return cacheDirectory;
            }
        }, diskCacheSize);
    }
}
