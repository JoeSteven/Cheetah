package com.joey.cheetah.core.media.scan

import android.Manifest
import android.app.Activity
import com.joey.cheetah.core.permission.PermissionListener
import com.joey.cheetah.core.permission.PermissionUtil
import com.joey.cheetah.core.utils.Jumper
import com.journeyapps.barcodescanner.CaptureActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.lang.IllegalStateException

/**
 * Description:
 * author:Joey
 * date:2018/10/17
 */
object ScanCodeHelper {
    var scanActivity:Class<out Activity> = CaptureActivity::class.java

    fun replaceScanActivity(clazz: Class<out Activity>) {
        scanActivity = clazz
    }

    private val subjects = HashMap<String, Subject<String>>()

    internal fun dispatchSuccess(key: String, path: String) {
        val subject = subjects[key]
        subjects.remove(key)
        if (subject != null && !subject.hasComplete()) {
            subject.onNext(path)
            subject.onComplete()
        }
    }

    internal fun disPatchError(key: String, e: Exception) {
        val subject = subjects[key]
        subjects.remove(key)
        if (subject != null && !subject.hasComplete()) {
            subject.onError(e)
        }
    }

    fun scan(activity: Activity): Single<String> {
        val subject = PublishSubject.create<String>().toSerialized()
        subjects[activity.toString()] = subject
        PermissionUtil.requestPermission(activity, object :PermissionListener{
            override fun permissionGranted(permission: Array<out String>) {
                Jumper.make(activity, ScanCodeActivity::class.java)
                        .putString("Scan_Result", activity.toString())
                        .jump()
            }

            override fun permissionDenied(permission: Array<out String>) {
                disPatchError(activity.toString(), IllegalStateException("该功能需要拍照权限！"))
            }

        }, Manifest.permission.CAMERA)
        return subject.toFlowable(BackpressureStrategy.BUFFER).singleOrError()
    }



}