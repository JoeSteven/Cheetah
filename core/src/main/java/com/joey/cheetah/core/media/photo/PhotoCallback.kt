package com.joey.cheetah.core.media.photo

/**
 * Description:
 * author:Joey
 * date:2018/10/19
 */
interface PhotoCallback {
    fun onSuccess(filePath: String)
    fun onFailed(e: Exception)

}