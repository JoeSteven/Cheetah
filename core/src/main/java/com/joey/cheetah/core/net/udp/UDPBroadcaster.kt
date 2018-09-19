package com.joey.cheetah.core.net.udp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.DhcpInfo
import android.net.wifi.WifiManager
import android.support.annotation.RequiresPermission
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.reflect.InvocationTargetException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Description:
 * author:Joey
 * date:2018/9/17
 */

class UDPBroadcaster(var context: Context) {
    var socket: DatagramSocket? = null

    fun start(localPort: Int) {
        if (socket != null) {
            stop()
        }
        socket = DatagramSocket(localPort)
        socket?.broadcast = true
        socket?.reuseAddress = true
    }

    fun stop() {
        if (socket != null && socket?.isClosed?.not() as Boolean) {
            socket?.close()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    fun send(data: ByteArray, broadcastPort: Int): Single<Boolean> {
        return Single.create<Boolean> {
            try {
                val address = getBroadcastAddress(context)
                val packet = DatagramPacket(data, data.size)
                packet.address = address
                packet.port = broadcastPort
                socket?.send(packet)
                if (!it.isDisposed) {
                    it.onSuccess(true)
                }
            } catch (e: Exception) {
                if (!it.isDisposed) {
                    it.onError(e)
                }
            }
        }
    }

    fun receive(buffer: ByteArray): Observable<DatagramPacket> {
        var recv = true
        return Observable.create<DatagramPacket>({
            while (socket != null && socket?.isClosed?.not() as Boolean) {
                try {
                    val packet = DatagramPacket(buffer.copyOf(), buffer.size)
                    socket?.receive(packet)
                    if (!it.isDisposed) {
                        it.onNext(packet)
                    } else {
                        break
                    }
                } catch (e: Exception) {
                    if (!it.isDisposed) {
                        it.onError(e)
                    }
                    e.printStackTrace()
                }
            }
        })

    }

    companion object {
        /**
         * 获取广播地址
         */
        @SuppressLint("WifiManagerPotentialLeak")
        @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
        fun getBroadcastAddress(context: Context): InetAddress {
            if (isWifiApEnabled(context)) { //判断wifi热点是否打开
                return InetAddress.getByName("192.168.43.255")  //直接返回
            }
            val wifi: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcp: DhcpInfo = wifi.dhcpInfo ?: return InetAddress.getByName("255.255.255.255")
            val broadcast = (dhcp.ipAddress and dhcp.netmask) or dhcp.netmask.inv()
            val quads = ByteArray(4)
            for (k in 0..3) {
                quads[k] = ((broadcast shr k * 8) and 0xFF).toByte()
            }
            return InetAddress.getByAddress(quads)
        }

        /**
         * check whether the wifiAp is Enable
         */
        @SuppressLint("WifiManagerPotentialLeak")
        private fun isWifiApEnabled(context: Context): Boolean {
            try {
                val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val method = manager.javaClass.getMethod("isWifiApEnabled")
                return method.invoke(manager) as Boolean
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            return false
        }
    }
}