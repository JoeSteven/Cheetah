package com.joey.cheetah.sample.udp

import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.mvp.auto.Presenter
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_udp.*

class UdpActivity : AbsActivity(), IUdpView {
    private val clientPort = 5000
    private val serverPort = 5051
    private var serverCount = 1
    private var clientCount = 1

    @Presenter
    private val serverPresenter = UdpServerPresenter(this)

    @Presenter
    private val clientPresenter = UdpClientPresenter(this)

    override fun initLayout(): Int {
        return R.layout.activity_udp
    }


    override fun initPresenter() {
        serverPresenter.start(serverPort)
        clientPresenter.start(clientPort)
    }

    override fun initView() {
        btnServerSend.setOnClickListener {
            serverPresenter.send("server data $serverCount", clientPort)
            serverCount++
        }
        btnClientSend.setOnClickListener {
            clientPresenter.send("client data $clientCount")
            clientCount++
        }
    }

    override fun sendMsg(msg: String) {
        tvServer.text = "send -> $msg"
    }

    override fun receiveMsg(msg: String) {
        tvClient.text = "receive -> $msg"
    }

}
