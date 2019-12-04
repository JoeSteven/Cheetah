package com.joey.cheetah.core.ui.loading

/**
 * Description: 加载接口
 * author:Joey
 * date:2019-06-13
 */
interface ILoadingView {
    fun startLoading(msg:String = "加载中")

    fun stopLoading()
}