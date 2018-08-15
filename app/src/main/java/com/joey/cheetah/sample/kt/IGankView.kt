package com.joey.cheetah.sample.kt

import com.joey.cheetah.mvp.IView

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
interface IGankView : IView{
    fun showContent(data: List<GankData> )
    fun loading()
    fun stopLoading()
}