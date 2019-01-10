package com.terminus.ereboot

import android.app.Activity
import android.app.Application
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * Description: 重启应用的配置
 * author:Joey
 * date:2019/1/10
 */
class ERebootConfig private constructor(val context: Application,
                                        val rebootActivity: Class<out Activity>,
                                        val logFile:String,
                                        val logDir:String,
                                        val crashWhenDebug: Boolean,
                                        val debug: Boolean,
                                        val listener: OnExceptionOccurListener,
                                        val writer: ExceptionLogWriter){
    // todo 异常发生时的回调，可以做一些重要数据的保存 done
    // todo 异常的判断 是否有不重启的异常 done
    // todo 重启的页面 done
    // todo 重启应用后的标记 done
    // todo crash记录日志地址 done
    // todo debug 模式
    companion object {
        /**
         * 构造一个 ERebootConfig.Builder
         */
        fun with(application: Application, rebootActivity: Class<out Activity>):Builder {
            return Builder(application, rebootActivity)
        }
    }

    fun debugMode():Boolean {
        return crashWhenDebug && debug
    }

    class Builder (private val context: Application, private val rebootActivity: Class<out Activity>) {
        /**
         * 设置调试模式
         * @param crashWhenDebug 是否在debug状态下正常崩溃，不重启
         * @param isDebug 当前是否是debug状态，一般用 BuildConfig.DEBUG
         * 默认关闭
         */
        fun setDebugMode(crashWhenDebug:Boolean, isDebug:Boolean):Builder {
            this.crashWhenDebug = crashWhenDebug
            debug = isDebug
            return this
        }

        /**
         * 设置调试模式
         * @param listener 崩溃发生时的回调监听，该接口回调的方法有
         *
         * onErrorOccurred -> 当异常发生时马上回调该方法，可以在该回调中进行一些重要数据的保存
         *
         * shouldReboot:Boolean -> 是否需要重启，如果某些异常不需要捕获直接crash则在该方法中返回逻辑
         *
         * onReboot -> 开始重启应用回调该方法，可以在该方法中去记录当前是重启状态，如果应用重启后有相应逻辑的话
         */
        fun setOnExceptionOccurListener(listener: OnExceptionOccurListener):Builder {
            this.listener = listener
            return this
        }

        /**
         * 设置日志路径
         * @param fileName 设置异常日志的文件名，默认为exception.log
         * @param fileDir 设置异常日志的文件路径，默认为Android/data/包名/cache/e-log/
         * 即整体默认为：Android/data/包名/cache/e-log/exception.log
         */
        fun setLogFile(fileName:String, fileDir:String): Builder {
            logFile = fileName
            logDir = fileDir
            return this
        }

        /**
         * 设置日志写入器
         * @param writer 日志写入器，如果需要将异常信息上报或者其他处理可以设置写入器
         * 默认将log写入到本地 logfile 中存储
         */
        fun setLogWriter(writer:ExceptionLogWriter):Builder {
            this.writer = writer
            return this
        }

        /**
         * 创建ERebootConfig
         */
        fun create():ERebootConfig {
            return ERebootConfig(context,
                    rebootActivity,
                    logFile,
                    logDir,
                    crashWhenDebug,
                    debug,
                    listener,
                    writer)
        }

        private var logFile = "exception.log"
        private var logDir = "${context.externalCacheDir.path}/e-log/"
        private var crashWhenDebug = false
        private var debug = false
        private var listener:OnExceptionOccurListener = object :OnExceptionOccurListener{
            override fun onErrorOccurred(e: Throwable, t: Thread) {}

            override fun shouldReboot(e: Throwable, t: Thread): Boolean { return true }

            override fun onReboot() {}
        }

        private var writer:ExceptionLogWriter = object: ExceptionLogWriter{
            override fun write(log: String, logFilePath:String, t: Thread, e: Throwable) {
                val file = File(logFilePath)
                if (!file.exists()) {
                    file.createNewFile()
                }
                val outWriter = OutputStreamWriter(FileOutputStream(file, true))
                outWriter.write(log)
                outWriter.close()
            }
        }

    }

    interface OnExceptionOccurListener{
        fun onErrorOccurred(e: Throwable, t: Thread)

        fun shouldReboot(e: Throwable,t: Thread):Boolean

        fun onReboot()
    }

    interface ExceptionLogWriter {
        fun write(log:String, logFilePath:String, t: Thread, e:Throwable)
    }

}