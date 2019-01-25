package com.joey.cheetah.sample.kt

import android.os.Bundle
import android.os.Environment
import com.joey.cheetah.core.media.ImageHelper
import com.joey.cheetah.mvp.AbsPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankPresenter(view: IGankView) : AbsPresenter<IGankView>(view) {
    private val repository = GankRepository()
    private var data: List<GankData> = ArrayList()

    fun queryAndroid() {
        repository.demoTest()
        add(repository.queryAndroid()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView?.loading() }
                .doOnSuccess {
                    mView?.stopLoading()
                    data = it
                }
                .doOnError { mView?.stopLoading() }
                .subscribe({ mView?.showContent(it) },
                        { mView?.toast("load data failed! $it") }))
    }

    fun querySurprise() {
        add(repository.querySurprise()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView?.loading() }
                .doOnSuccess {
                    mView?.stopLoading()
                    data = it
                }
                .doOnError { mView?.stopLoading() }
                .subscribe({ mView?.showContent(it) },
                        { mView?.toast("load data failed! $it") }))
    }

    override fun onSaveData(outState: Bundle?) {
        outState?.putParcelableArrayList("Gank_Data", data as ArrayList)
    }

    override fun onRestoredData(savedInstanceState: Bundle?) {
        mView?.apply {
            loading()
            data = savedInstanceState?.getParcelableArrayList("Gank_Data") ?: ArrayList()
            stopLoading()
            showContent(data)
        }
    }

    fun download(url: String?) {
        if (url == null) return
        Observable.create<File> { emitter ->
            var dir = File(Environment.getExternalStorageDirectory().path + "/Cheetah")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            var name = url.split("/")
            val path = Environment.getExternalStorageDirectory().path + "/Cheetah/" + name[name.size - 1]
            val file = ImageHelper.saveImageIntoGallery(url, path)
            if (file != null) {
                emitter.onNext(file)
                emitter.onComplete()
            } else {
                emitter.onError(Throwable("error!"))
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView?.toast("start download") }
                .doOnError { e -> e.printStackTrace() }
                .subscribe({ t: File? -> mView?.toast("download success ${t?.absolutePath}") }, { mView?.toast("download failed") })
    }
}