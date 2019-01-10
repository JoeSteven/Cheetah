package com.terminus.ereboot

import android.app.Activity
import android.content.Intent
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:异常捕获及重启处理器
 * author:Joey
 * date:2019/1/10
 */
object ERebootUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private val activityList = ArrayList<Activity>()
    private var config: ERebootConfig? = null

    fun init(rebootConfig: ERebootConfig) {
        this.config = rebootConfig
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler(this)
        config?.context?.registerActivityLifecycleCallbacks(ExceptionLifecycleCallbacks())

        val dir = File(config?.logDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        // 调试模式下均崩溃
        if (config?.debugMode() != null && config!!.debugMode()) {
            defaultHandler?.uncaughtException(t, e)
            return
        }
        config?.listener?.onErrorOccurred(e, t)
        val shouldReboot = config?.listener?.shouldReboot(e, t)
        try {
            e.printStackTrace()
            writeLogFile(t, e)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (shouldReboot != null && shouldReboot) {
                // 重启过程
                config?.listener?.onReboot()
                rebootActivity()
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(0)
            } else {
                defaultHandler?.uncaughtException(t, e)
            }
        } catch (e: Exception) {
            defaultHandler?.uncaughtException(t, e)
        }
    }

    private fun rebootActivity() {
        for (index in activityList.indices) {
            if (index == activityList.size - 1) {
                val activity = activityList[index]
                activity.startActivity(Intent(activity, config!!.rebootActivity))
                activity.finish()
            } else {
                activityList[index].finish()
            }
        }
        activityList.clear()
    }

    private fun writeLogFile(t: Thread, e: Throwable) {
        val buffer = StringBuffer()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)
                .format(Date(System.currentTimeMillis()))
        buffer.append("$time =================================>\n")
        buffer.append("Error occurred in Thread:$t \n")
        buffer.append("Caused by:\n")
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)
        var cause = e.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        buffer.append(writer.toString())
        buffer.append("$time =================================>\n")
        buffer.append("    \n")
        config?.writer?.write(buffer.toString(), config!!.logDir+ config!!.logFile, t, e)
    }

    fun remove(activity: Activity?) {
        if (activity != null) {
            activityList.remove(activity)
        }
    }

    fun add(activity: Activity?) {
        if (activity != null) {
            activityList.add(activity)
        }
    }
}