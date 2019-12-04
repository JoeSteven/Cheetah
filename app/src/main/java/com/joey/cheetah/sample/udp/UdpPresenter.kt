package com.joey.cheetah.sample.udp

import androidx.lifecycle.LifecycleOwner
import android.os.Bundle
import com.joey.cheetah.core.async.schedulers.SchedulersHelper
import com.joey.cheetah.core.global.Global
import com.joey.cheetah.core.net.udp.UDPBroadcaster
import com.joey.cheetah.mvp.AbsPresenter
import java.net.DatagramPacket

/**
 * Description:
 * author:Joey
 * date:2018/9/17
 */
open class UdpPresenter(view: IUdpView) : AbsPresenter<IUdpView>(view) {
    private val udpRepo = UDPBroadcaster(Global.context())

    fun start(localPort: Int) {
        udpRepo.start(localPort)
        receive()
    }

    fun stop() {
        udpRepo.stop()
    }

    fun send(data:String, destPort: Int) {
        add(udpRepo.send(data.toByteArray(), destPort)
                .compose(SchedulersHelper.io_main())
                .subscribe({ mView?.sendMsg("send:$data success:$it") },
                        {mView?.sendMsg("send error:$it")}))
    }

    fun receive() {
        add(udpRepo.receive(ByteArray(1024))
                .compose(SchedulersHelper.io_main())
                .doOnNext(this::onReceiveNext)
                .subscribe({mView?.receiveMsg("receive ${String(it.data)}")}, {mView?.receiveMsg("receive error:$it")}))
    }

    open fun onReceiveNext(packet: DatagramPacket) {

    }

    override fun onSaveData(outState: Bundle?) {

    }

    override fun onRestoredData(savedInstanceState: Bundle?) {

    }

    override fun onDestroyed(owner: LifecycleOwner) {
        super.onDestroyed(owner)
        stop()
    }
}