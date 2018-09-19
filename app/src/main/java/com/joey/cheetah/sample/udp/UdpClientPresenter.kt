package com.joey.cheetah.sample.udp

import java.net.DatagramPacket

/**
 * Description:
 * author:Joey
 * date:2018/9/17
 */
class UdpClientPresenter(view: IUdpView) : UdpPresenter(view) {
    var serverPort:Int = 0
    override fun onReceiveNext(packet: DatagramPacket) {
        serverPort = packet.port
    }

    fun send(data:String) {
        if (serverPort == 0) {
            mView?.sendMsg("client can't send data without server port!")
            return
        }
        send(data, serverPort)
    }

}