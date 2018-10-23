package com.joey.cheetah.core.media.photo

import android.app.Activity
import com.joey.cheetah.core.utils.Jumper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * Description:
 * author:Joey
 * date:2018/10/19
 */
object PhotoGetter {

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

    fun getPhotoByCamera(activity: Activity): Flowable<String> {
        return generateFlowable(activity, PhotoGetActivity.TAKE_PHOTO)
    }

    fun getPhotoByGallery(activity: Activity): Flowable<String> {
        return generateFlowable(activity, PhotoGetActivity.SELECT_PHOTO)
    }

    private fun generateFlowable(activity: Activity, type: Int): Flowable<String> {
        val subject = PublishSubject.create<String>().toSerialized()
        subjects[activity.toString()] = subject
        Jumper.make(activity, PhotoGetActivity::class.java)
                .putString("subject_key", activity.toString())
                .putInt("type", type)
                .jump()
        return subject.toFlowable(BackpressureStrategy.BUFFER)
    }
}