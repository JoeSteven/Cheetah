package com.joey.cheetah.core.net.util

import android.util.SparseArray
import com.joey.cheetah.core.async.IAsyncExecutor
import com.joey.cheetah.core.async.RxJavaExecutor
import com.joey.cheetah.core.ktextension.logE

/**
 * Description:
 * author:Joey
 * date:2018/9/27
 */
class TimeoutChecker<in T> (private val timeout: Long, private val callback:(frame: T) -> Unit) {

    private val taskExecutor = RxJavaExecutor()
    private val timeoutMap = SparseArray<TimeOutTask<T>>()

    private val timeoutCallback = object : IAsyncExecutor.IAsyncResultCallback<T> {
        override fun done(frame: T) {
            callback(frame)
        }

        override fun error(e: Throwable?) {}
    }

    //计算超时
    fun countTimeout(id: Int, frame: T) {
        val task = TimeOutTask(frame)
        timeoutMap.put(id, task)
        taskExecutor.execute(task, timeoutCallback, timeout)
    }

    fun isTimeout(id: Int): Boolean {
        val task = timeoutMap[id] ?: return false
        return if (task.isTimeout){
            timeoutMap.remove(id)
            logE("TimeoutChecker", "timeout for request id:$id")
            true
        } else {
            task.cancel()
            timeoutMap.remove(id)
            logE("TimeoutChecker", "request $id didn't timeout")
            false
        }
    }

    fun cancel(id: Int) {
        timeoutMap[id]?.cancel()
    }

    fun cancelAll() {
        for (i in 0 until timeoutMap.size()) {
            timeoutMap[timeoutMap.keyAt(i)].cancel()
        }
        timeoutMap.clear()
    }

}