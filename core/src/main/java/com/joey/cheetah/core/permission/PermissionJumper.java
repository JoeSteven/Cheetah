package com.joey.cheetah.core.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

/**
 * Description: 系统权限设置页面跳转工具
 * author:Joey
 * date:2019/1/17
 */
class PermissionJumper {
    public static boolean jumpToSettings(@NonNull Activity activity) {
        try {
            Intent intent = new Intent();
            String packageName = activity.getPackageName();
            String phoneBrand = Build.MANUFACTURER;
            switch (phoneBrand) {
                case "HUAWEI":
                    intent = jumpHuawei(intent, packageName);
                    break;
                case "Meizu":
                    intent = jumpMeizu(intent, packageName);
                    break;
                case "Xiaomi":
                    intent = jumpXiaomi(intent, packageName);
                    break;
                case "Sony":
                    intent = jumpSony(intent, packageName);
                    break;
                case "OPPO":
                    intent = jumpOPPO(intent, packageName);
                    break;
                case "LG":
                    intent = jumpLG(intent, packageName);
                    break;

//            case "vivo":
//                goVivoMainager();
//                break;
//
//            case "Coolpad":
//                goCoolpadMainager();
//                break;
//
//
//            case "samsung":
//                goSangXinMainager();
//                break;

                default:
                    intent = goIntentSetting(activity, intent);
                    break;
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static Intent jumpOnePlus(Intent intent, String packageName) {
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        ComponentName comp = new ComponentName("com.oneplus.security", "com.oneplus.security.oppermission.OPPermissionAppPermListActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static Intent jumpLG(Intent intent, String packageName) {
        intent.setAction("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static Intent jumpOPPO(Intent intent, String packageName) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static Intent jumpSony(Intent intent, String packageName) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static Intent jumpXiaomi(Intent intent, String packageName) {
        intent.setAction("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", packageName);
        return intent;
    }

    private static Intent jumpMeizu(Intent intent, String packageName) {
        intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", packageName);
        return intent;
    }

    private static Intent jumpHuawei(Intent intent, String packageName) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", packageName);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        return intent;
    }

    private static Intent goIntentSetting(Activity activity, Intent intent) {
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        return intent;
    }
}
