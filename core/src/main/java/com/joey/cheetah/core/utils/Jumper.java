package com.joey.cheetah.core.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Description: an util to open another activity
 * author:Joey
 * date:2018/7/26
 */
public class Jumper {
    private Intent intent = null;
    private Activity from;

    private Jumper(Activity from, Class<? extends Activity> to, String action) {
        this.intent = new Intent();
        this.from = from;
        if (to != null) {
            intent.setClass(from, to);
        }
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
    }

    private Jumper(Activity from, Intent intent) {
        this.intent = intent;
        this.from = from;
    }

    public static Jumper make(Activity from, Intent intent) {
        return new Jumper(from, intent);
    }

    public static Jumper make(Activity from, Class<? extends Activity> to) {
        return new Jumper(from, to, "");
    }

    public static Jumper make() {
        return new Jumper(null, null, null);
    }

    public static Jumper make(Activity from, String action) {
        return new Jumper(from, null, action);
    }

    public Jumper setAction(String action) {
        intent.setAction(action);
        return this;
    }

    public Jumper addFlag(int flag) {
        intent.addFlags(flag);
        return this;
    }

    public Jumper addCategory(String category) {
        intent.addCategory(category);
        return this;
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

    public Jumper putArrayList(String key, ArrayList<? extends Parcelable> list) {
        intent.putParcelableArrayListExtra(key, list);
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    /**
     * 跳转到某个页面
     */
    public void jump() {
        from.startActivity(intent);
    }

    public void jumpForResult(int requestCode) {
        from.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转并销毁当前页面
     */
    public void jumpAndFinish() {
        from.startActivity(intent);
        from.finish();
    }
}
