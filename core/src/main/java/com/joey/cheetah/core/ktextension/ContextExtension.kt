package com.joey.cheetah.core.ktextension

import android.content.Context
import com.joey.cheetah.core.global.Global
import com.joey.cheetah.core.utils.UIUtil

/**
 * Description:
 * author:Joey
 * date:2018/9/7
 */

fun Context.versionCode(pkgName: String = packageName): Int {
    return Global.versionCode(pkgName)
}

fun Context.versionName(pkgName: String = packageName): String {
    return Global.versionName(pkgName)
}

fun Context.isAppForeGround(pkgName: String = packageName): Boolean {
    return Global.isAppForeground(packageName)
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels


fun Context.dp2px(value: Int): Int {
    return UIUtil.dp2px(value)
}

fun Context.px2dp(value: Int): Int {
    return UIUtil.px2dp(value)
}

fun Context.sp2px(value: Int): Int {
    return UIUtil.sp2px(value)
}

fun Context.px2sp(value: Int): Int {
    return UIUtil.px2sp(value)
}