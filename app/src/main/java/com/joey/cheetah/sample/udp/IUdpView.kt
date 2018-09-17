package com.joey.cheetah.sample.udp

import com.joey.cheetah.mvp.IView

/**
 * Description:
 * author:Joey
 * date:2018/9/17
 */
interface IUdpView : IView{
    fun sendMsg(msg: String)
    fun receiveMsg(msg: String)
}