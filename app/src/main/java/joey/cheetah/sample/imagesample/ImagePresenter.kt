package joey.cheetah.sample.imagesample

import android.os.Bundle
import cheetah.core.async.AsyncManger
import cheetah.core.async.IAsyncExecutor
import cheetah.core.media.ImageHelper
import cheetah.core.mvp.AbsPresenter
import java.io.File
import java.util.*

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
class ImagePresenter(view: IImageShowView?) : AbsPresenter<IImageShowView>(view) {
    private var datas: MutableList<String> = ArrayList()
    private var index = 0

    override fun onSaveData(outState: Bundle?) {

    }

    override fun onRestoredData(savedInstanceState: Bundle?) {

    }

    fun fetchData() {
        datas.add("http://img.hb.aicdn.com/83371ed4ef6bc9ec6333258bd472639785fa754a12b26-r8iVIG_sq320")
        datas.add("http://img.hb.aicdn.com/380533e59fed767ee4f658e14eb0c1290812de501a769-Ro8nCh_sq320")
        datas.add("http://img.hb.aicdn.com/aded9d9975970578e3218674fc95d4594e49ab8810842-1dR3Mj_sq320")
        show()
    }

    private fun show() {
        if (isValid) {
            mView.show(datas[index])
            index++
            if (index >= datas.size) index = 0
        }
    }

    fun next() {
        show()
    }

    fun clear() {
        // 单页面任务，页面等待执行完成才被销毁
        async().execute({ ImageHelper.clearDiskCaches() },
                object : IAsyncExecutor.IAsyncCallback {
                    override fun done() {
                        if (isValid) mView.toast("clear cache success")
                    }

                    override fun error(e: Throwable?) {
                        if (isValid) mView.toast("clear cache error + ${e.toString()}")
                    }
                })
    }

    fun save() {
        // 全局任务，即使页面销毁，也要继续执行
        AsyncManger.obtain()
                .execute({ ImageHelper.saveImageIntoGallery(datas[index], null) },
                        object : IAsyncExecutor.IAsyncResultCallback<File> {
                            override fun done(t: File) {
                                if (isValid) mView.toast("save this picture to gallery success!" + t.absolutePath)
                            }

                            override fun error(e: Throwable?) {
                                if (isValid) mView.toast("save this picture to gallery error + ${e.toString()}")
                            }
                        })
    }

}