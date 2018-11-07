package com.joey.cheetah.core.ktextension

import com.joey.cheetah.core.utils.CLog

/**
 * Description:
 * author:Joey
 * date:2018/9/12
 */

fun Any.logV(tag:String, msg:String) {
    CLog.v(tag, CLog.msgWithclassName(this, msg))
}
fun Any.logD(tag:String, msg:String) {
    CLog.d(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logI(tag:String, msg:String) {
    CLog.i(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logW(tag:String, msg:String) {
    CLog.w(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logE(tag:String, msg:String) {
    CLog.e(tag, CLog.msgWithclassName(this, msg))
}

