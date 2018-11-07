package com.joey.cheetah.core.net.util

import com.joey.cheetah.core.async.IAsyncExecutor

/**
 * Description:
 * author:Joey
 * date:2018/9/26
 */
class TImeOutTask<T>(private val frame: T) : IAsyncExecutor.AsyncResultTask<T> {
    @Volatile
    var isTimeout = false
        private set

    @Volatile
    private var cancel = false

    override fun run(): T {
        if (!cancel) {
            isTimeout = true
            return frame
        }
        throw IllegalStateException("Timeout task for $frame is canceled!")
    }

    fun cancel() {
        cancel = true
    }

}