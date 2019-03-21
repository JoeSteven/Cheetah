package com.joey.cheetah.core.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

/**
 * /**
 * Description: Util to request permission
 * author:Joey
 * date:2018/7/30
 */

public class PermissionUtil {

    private static final String TAG = "PermissionGrantor";
    private static HashMap<String, PermissionListener> listenerMap = new HashMap();
    final static String defaultTitle = "帮助";
    final static String defaultContent = "当前应用缺少必要权限。\n\n请点击 \"设置\"-\"权限\"-打开所需权限。因为安卓手机种类繁多，本次跳转可能失败，如果失败请手动设置。";
    final static String defaultCancel = "取消";
    final static String defaultEnsure = "设置";

    /**
     * 申请授权，当用户拒绝时，会显示默认一个默认的Dialog提示用户
     *
     * @param context
     * @param listener
     * @param permission 要申请的权限
     */
    public static void requestPermission(Activity context, PermissionListener listener, String... permission) {
        requestPermission(context, listener, permission, true, null);
    }

    /**
     * 申请授权，当用户拒绝时，可以设置是否显示Dialog提示用户，也可以设置提示用户的文本内容
     *
     * @param context
     * @param listener
     * @param permission 需要申请授权的权限
     * @param showTip    当用户拒绝授权时，是否显示提示
     * @param tip        当用户拒绝时要显示Dialog设置
     */
    public static void requestPermission(@NonNull Activity context, @NonNull PermissionListener listener
            , @NonNull String[] permission, boolean showTip, @Nullable TipInfo tip) {

        if (listener == null) {
            Log.e(TAG, "listener is null");
            return;
        }

        if (PermissionUtil.hasPermission(context, permission)) {
            listener.permissionGranted(permission);
        } else {
            if (Build.VERSION.SDK_INT < 23) {
                listener.permissionDenied(permission);
            } else {
                String key = String.valueOf(System.currentTimeMillis());
                listenerMap.put(key, listener);
                Intent intent = new Intent(context, PermissionActivity.class);
                intent.putExtra("permission", permission);
                intent.putExtra("key", key);
                intent.putExtra("showTip", showTip);
                intent.putExtra("tip", tip);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        }
    }


    /**
     * 判断权限是否授权
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(@NonNull Activity context, @NonNull String... permissions) {

        if (permissions.length == 0) {
            return false;
        }

        for (String per : permissions) {
            int result = PermissionChecker.checkSelfPermission(context, per);
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断一组授权结果是否为授权通过
     *
     * @param grantResult
     * @return
     */
    public static boolean isGranted(@NonNull int... grantResult) {

        if (grantResult.length == 0) {
            return false;
        }

        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 跳转到当前应用对应的设置页面
     *
     * @param context
     */
    public static void gotoSetting(@NonNull Activity context) {
        gotoSetting(context, new TipInfo(null, null, null, null));
    }

    public static void gotoSetting(@NonNull Activity context, String hint) {
        TipInfo tipInfo = new TipInfo(null, hint, null, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(TextUtils.isEmpty(tipInfo.title) ? defaultTitle : tipInfo.title);
        builder.setMessage(TextUtils.isEmpty(tipInfo.content) ? defaultContent : tipInfo.content);
        builder.setNegativeButton(TextUtils.isEmpty(tipInfo.cancel) ? defaultCancel : tipInfo.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(TextUtils.isEmpty(tipInfo.ensure) ? defaultEnsure : tipInfo.ensure, (dialog, which) -> {
            PermissionJumper.jumpToSettings(context);
            dialog.dismiss();
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static void gotoSetting(@NonNull Activity context, TipInfo tipInfo) {
        PermissionJumper.jumpToSettings(context);
    }

    /**
     * @param key
     * @return
     */
    static PermissionListener fetchListener(String key) {
        return listenerMap.remove(key);
    }


    public static class TipInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        String title;
        String content;
        String cancel;  //取消按钮文本
        String ensure;  //确定按钮文本

        public TipInfo(@Nullable String title, @Nullable String content, @Nullable String cancel, @Nullable String ensure) {
            this.title = title;
            this.content = content;
            this.cancel = cancel;
            this.ensure = ensure;
        }
    }
}
