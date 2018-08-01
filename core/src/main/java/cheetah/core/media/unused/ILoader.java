package cheetah.core.media.unused;

import android.content.Context;

import cheetah.core.media.unused.ImageConfig;

/**
 * Description:unused
 * author:Joey
 * date:2018/7/30
 */
public interface ILoader {

    /**
     * to init image loader
     * @param cacheSize unit is MB
     */
    void init(Context context, String diskCachePath, long cacheSize);

    void request(ImageConfig config);

    void pause();

    void resume();

    void clearDiskCaches();

    void clearMemoryCaches();

    boolean isCached(String url);

    void trimMemory(int level);

    void saveImageIntoGallery(String url, String savePath, String saveName);
}
