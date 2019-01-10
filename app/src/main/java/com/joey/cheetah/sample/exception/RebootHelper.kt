package com.joey.cheetah.sample.exception

import com.joey.cheetah.core.global.Global
import com.joey.cheetah.core.ktextension.logE
import com.joey.cheetah.core.storage.SharedPrefHelper
import com.terminus.ereboot.ERebootConfig
import java.lang.NullPointerException

/**
 * Description:
 * author:Joey
 * date:2019/1/10
 */
object RebootHelper:ERebootConfig.OnExceptionOccurListener {
    var isRebooted = initReboot()

    private fun initReboot():Boolean {
        val reboot = SharedPrefHelper.from(Global.context()).getBoolean("is_reboot", false)
        SharedPrefHelper.from(Global.context()).apply("is_reboot", false)
        return reboot
    }

    override fun onErrorOccurred(e: Throwable, t: Thread) {
        logE("Reboot", "error occurred")
    }

    override fun shouldReboot(e: Throwable, t: Thread): Boolean {
        if (e is NullPointerException) return false
        return true
    }

    override fun onReboot() {
        isRebooted = true
        SharedPrefHelper.from(Global.context()).sharePreferences.edit().putBoolean("is_reboot", true).commit()
    }

}