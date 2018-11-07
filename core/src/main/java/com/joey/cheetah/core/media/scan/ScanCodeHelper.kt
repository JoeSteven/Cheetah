package com.joey.zxingdemo

import android.app.Activity
import com.joey.cheetah.core.media.scan.ScanCodeActivity
import com.joey.cheetah.core.utils.Jumper
import com.journeyapps.barcodescanner.CaptureActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

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
        }
    }

    internal fun disPatchError(key: String, e: Exception) {
        val subject = subjects[key]
        subjects.remove(key)
        if (subject != null && !subject.hasComplete()) {
            subject.onError(e)
        }
    }

    fun scan(activity: Activity): Flowable<String>? {
        val subject = PublishSubject.create<String>().toSerialized()
        subjects[activity.toString()] = subject
        Jumper.make(activity, ScanCodeActivity::class.java)
                .putString("Scan_Result", activity.toString())
                .jump()
        return subject.toFlowable(BackpressureStrategy.BUFFER)
    }



}