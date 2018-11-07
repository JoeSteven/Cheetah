package com.joey.cheetah.sample.java.connect

import android.text.TextUtils
import com.joey.cheetah.core.media.ble.BleSplitData
import com.joey.rxble.RxBle
import com.polidea.rxandroidble2.NotificationSetupMode
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Description:
 * author:Joey
 * date:2018/11/1
 */
class BleRepository {
    val operator = RxBle.create()!!
    private var response: StringBuilder? = null
    private var responseID = "-1"
    private var notifyDisposable: Disposable? = null

    companion object {
        const val BLE_08 = "08"
        const val BLE_03 = "03"
        const val BLE_44 = "44"
        const val BLE_46 = "46"
        const val BLE_63 = "63"
        const val BLE_99 = "99"
        const val BLE_END = "OVER"
        val BLE_IDS = arrayOf(BLE_08, BLE_03, BLE_44, BLE_46,BLE_63, BLE_99)
    }

    private fun canHandle(id: String): Boolean {
        return BLE_IDS.contains(id)
    }

    fun enable(): Observable<RxBleClient> {

        return operator.enable()
    }

    fun disable() {
        operator.disable()
    }

    fun scan(scanSettings: ScanSettings = ScanSettings.Builder().build(), vararg filters: ScanFilter): Observable<ScanResult> {
        return operator.scan(scanSettings, *filters)
    }

    fun stopScan() {
        operator.stopScan()
    }

    fun disconnect() {
        operator.disconnect()
    }

    fun connect(mac: String): Observable<RxBleConnection> {
        return operator.setConnectRetryTimes(3)
                .connect(mac)
    }

    fun getBondedDevices(): MutableSet<RxBleDevice>? {
        return RxBle.client().bondedDevices
    }

    fun writeCharacteristic(mac: String, uuid: UUID, data: BleSplitData): Single<ByteArray> {
        var single = operator.writeCharacteristic(mac, uuid, data.dataList[0])
        for (i in 1..(data.dataList.size - 1)) {
            single = single.flatMap { operator.writeCharacteristic(mac, uuid, data.dataList[i]) }
        }
        return single.flatMap { operator.writeCharacteristic(mac, uuid, data.endFrame) }
    }

    fun notifyCharacteristic(mac: String, uuid: UUID): Observable<BleResponse> {
        return Observable.create<BleResponse> {
            val emitter = it
            notifyDisposable = operator.setupNotification(mac, uuid, NotificationSetupMode.DEFAULT)
                    .flatMap { it }
                    .subscribe({
                        if (!emitter.isDisposed) {
                            if (packResponse(it)) {
                                // 下发包
                                emitter.onNext(BleResponse(responseID, response.toString()))
                                resetResponse()
                            }
                        } else {
                            resetResponse()
                        }
                    }, {
                        resetResponse()
                        if (!emitter.isDisposed) emitter.onError(it)
                    })
        }.doOnDispose {
            notifyDisposable?.dispose()
        }
    }

    fun release() {
        operator.release()
    }

    private fun resetResponse() {
        responseID = "-1"
        response = null
    }

    private fun packResponse(it: ByteArray): Boolean {
        val pac = String(it)
        // 需要判断开头
        if (TextUtils.equals("-1", responseID) && response == null) {
            responseID = "-1"
            response = null
            val id = pac.substring(0, 2)
            responseID = id
            response = StringBuilder()
            response?.append(pac.substring(2))
        } else {
            //是中间包
            response?.append(pac)
        }
        // 判断包是否结束
        if (response!!.contains(BLE_END)) {
            response = StringBuilder(response!!.substring(0,response!!.indexOf(BLE_END)))
            return true
        }
        return false
    }
}