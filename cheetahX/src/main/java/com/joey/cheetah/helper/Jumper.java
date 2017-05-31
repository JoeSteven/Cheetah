package com.joey.cheetah.helper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;


import com.joey.cheetah.core.base.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * description - 页面跳转帮助类
 * <p/>
 * author - Joe.
 * create on 16/7/15.
 * change
 * change on .
 */
public class Jumper {
    private Intent intent = null;

    private Jumper() {
        this.intent = new Intent();
    }

    public static Jumper getInstance() {
        return new Jumper();
    }

    public Jumper putInt(String key, int value) {
        intent.putExtra(key, value);
        return this;
    }

    public Jumper putLong(String key, long value) {
        intent.putExtra(key, value);
        return this;
    }

    public Jumper putString(String key, String value) {
        intent.putExtra(key, value);
        return this;
    }

    public Jumper putBundle(Bundle bundle) {
        intent.putExtras(bundle);
        return this;
    }

    public Jumper putSerializable(String key, Serializable value) {
        intent.putExtra(key, value);
        return this;
    }

    public Jumper putParcelable(String key, Parcelable value) {
        intent.putExtra(key, value);
        return this;
    }
    public Jumper putBoolean(String key, boolean value) {
        intent.putExtra(key, value);
        return this;
    }

    public Jumper putArrayList(String key, ArrayList<? extends Parcelable> list){
        intent.putParcelableArrayListExtra(key,list);
        return this;
    }
    /**跳转到某个页面*/
    public void jumpTo(BaseActivity from, Class<? extends BaseActivity> to){
        intent.setClass(from,to);
        from.startActivity(intent);
    }

    /**跳转并销毁当前页面*/
    public void jumpAndFinish(BaseActivity from, Class<? extends BaseActivity> to){
        intent.setClass(from,to);
        from.startActivity(intent);
        from.finish();
    }
}
