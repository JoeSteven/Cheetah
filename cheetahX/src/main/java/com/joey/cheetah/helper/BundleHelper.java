package com.joey.cheetah.helper;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * description - 参数帮助类
 * <p/>
 * author - Joe.
 * create on 16/8/8.
 * change
 * change on .
 */


public class BundleHelper {
    private Bundle bundle = null;

    private BundleHelper(){
        bundle = new Bundle();
    }

    public static BundleHelper getInstance(){
        return new BundleHelper();
    }

    public BundleHelper putString(String key, String value){
        bundle.putString(key,value);
        return this;
    }

    public BundleHelper putInt(String key, int value){
        bundle.putInt(key,value);
        return this;
    }

    public BundleHelper putBoolean(String key, boolean value){
        bundle.putBoolean(key,value);
        return this;
    }

    public BundleHelper putParcelable(String key, Parcelable value){
        bundle.putParcelable(key,value);
        return this;
    }

    public BundleHelper putBundle(String key, Bundle value){
        bundle.putBundle(key,value);
        return this;
    }

    public Bundle getBundle(){
        return bundle;
    }

    /**
     * 给 Fragment 传参
     * @param target 目标 Fragment
     */
    public void passFragment(Fragment target){
        target.setArguments(bundle);
    }
}
