package com.joey.cheetah.core.utils;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

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

    public static BundleMaker start() {
        return new BundleMaker();
    }

    public Bundle make() {
        return bundle;
    }

    private BundleMaker putAll(Bundle bundle) {
        bundle.putAll(bundle);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BundleMaker putAll(PersistableBundle bundle) {
        bundle.putAll(bundle);
        return this;
    }


    public BundleMaker putBoolean(@Nullable String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleMaker putInt(@Nullable String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleMaker putLong(@Nullable String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    public BundleMaker putDouble(@Nullable String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }

    public BundleMaker putString(@Nullable String key, @Nullable String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleMaker putBooleanArray(@Nullable String key, @Nullable boolean[] value) {
        bundle.putBooleanArray(key, value);
        return this;
    }

    public BundleMaker putIntArray(@Nullable String key, @Nullable int[] value) {
        bundle.putIntArray(key, value);
        return this;
    }

    public BundleMaker putLongArray(@Nullable String key, @Nullable long[] value) {
        bundle.putLongArray(key, value);
        return this;
    }

    public BundleMaker putDoubleArray(@Nullable String key, @Nullable double[] value) {
        bundle.putDoubleArray(key, value);
        return this;
    }

    public BundleMaker putStringArray(@Nullable String key, @Nullable String[] value) {
        bundle.putStringArray(key, value);
        return this;
    }

    public BundleMaker putByte(@Nullable String key, byte value) {
        bundle.putByte(key, value);
        return this;
    }


    public BundleMaker putChar(@Nullable String key, char value) {
        bundle.putChar(key, value);
        return this;
    }

    public BundleMaker putShort(@Nullable String key, short value) {
        bundle.putShort(key, value);
        return this;
    }

    public BundleMaker putFloat(@Nullable String key, float value) {
        bundle.putFloat(key, value);
        return this;
    }

    public BundleMaker putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        bundle.putCharSequence(key, value);
        return this;
    }

    public BundleMaker putParcelable(@Nullable String key, @Nullable Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BundleMaker putSize(@Nullable String key, @Nullable Size value) {
        bundle.putSize(key, value);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BundleMaker putSizeF(@Nullable String key, @Nullable SizeF value) {
        bundle.putSizeF(key, value);
        return this;
    }

    public BundleMaker putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        bundle.putParcelableArray(key, value);
        return this;
    }

    public BundleMaker putParcelableArrayList(@Nullable String key,
                                              @Nullable ArrayList<? extends Parcelable> value) {
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    public BundleMaker putSparseParcelableArray(@Nullable String key,
                                                @Nullable SparseArray<? extends Parcelable> value) {
        bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public BundleMaker putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    public BundleMaker putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        bundle.putStringArrayList(key, value);
        return this;
    }

    public BundleMaker putCharSequenceArrayList(@Nullable String key,
                                                @Nullable ArrayList<CharSequence> value) {
        bundle.putCharSequenceArrayList(key, value);
        return this;
    }


    public BundleMaker putSerializable(@Nullable String key, @Nullable Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public BundleMaker putByteArray(@Nullable String key, @Nullable byte[] value) {
        bundle.putByteArray(key, value);
        return this;
    }

    public BundleMaker putShortArray(@Nullable String key, @Nullable short[] value) {
        bundle.putShortArray(key, value);
        return this;
    }

    public BundleMaker putCharArray(@Nullable String key, @Nullable char[] value) {
        bundle.putCharArray(key, value);
        return this;
    }

    public BundleMaker putFloatArray(@Nullable String key, @Nullable float[] value) {
        bundle.putFloatArray(key, value);
        return this;
    }

    public BundleMaker putCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        bundle.putCharSequenceArray(key, value);
        return this;
    }

    public BundleMaker putBundle(@Nullable String key, @Nullable Bundle value) {
        bundle.putBundle(key, value);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BundleMaker putBinder(@Nullable String key, @Nullable IBinder value) {
        bundle.putBinder(key, value);
        return this;
    }
}
