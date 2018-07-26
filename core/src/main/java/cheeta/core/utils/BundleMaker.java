package cheeta.core.utils;

import android.os.Bundle;

/**
 * Description: A helper used to add params in bundle easily
 * author:Joey
 * date:2018/7/26
 */
public class BundleMaker {
    private Bundle bundle;

    private BundleMaker() {
        bundle = new Bundle();
    }

    public static BundleMaker put(String key, Object value) {
        BundleMaker bundleMaker = new BundleMaker();
        bundleMaker.putParams(key, value);
        return bundleMaker;
    }

    private BundleMaker putParams(String key, Object value) {
        if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        }
        return this;
    }

    public Bundle make() {
        return bundle;
    }
}
