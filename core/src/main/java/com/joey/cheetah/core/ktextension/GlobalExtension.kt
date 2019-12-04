package com.joey.cheetah.core.ktextension

import com.joey.cheetah.core.utils.CLog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.joey.cheetah.core.ui.CToast
import com.joey.cheetah.core.ui.DialogHelper
import com.joey.cheetah.core.ui.loading.ILoadingView
import com.uber.autodispose.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.parallel.ParallelFlowable
/**
 * Description:
 * author:Joey
 * date:2018/9/12
 */

/**
 * Description:
 * author:Joey
 * date:2019-07-03
 */

@CheckReturnValue
fun <T> Flowable<T>.autoDisposable(owner: LifecycleOwner): FlowableSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))

@CheckReturnValue
fun <T> Observable<T>.autoDisposable(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))

@CheckReturnValue
fun <T> Single<T>.autoDisposable(owner: LifecycleOwner): SingleSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))

@CheckReturnValue
fun <T> Maybe<T>.autoDisposable(owner: LifecycleOwner): MaybeSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))

@CheckReturnValue
fun Completable.autoDisposable(owner: LifecycleOwner): CompletableSubscribeProxy =
        this.`as`(AutoDispose.autoDisposable<Any>(AndroidLifecycleScopeProvider.from(owner)))

@CheckReturnValue
fun <T> ParallelFlowable<T>.autoDisposable(owner: LifecycleOwner): ParallelFlowableSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))

/************************loading***************/
@CheckReturnValue
fun <T> Flowable<T>.loading(loadingView: ILoadingView, msg: String = "加载中"): Flowable<T> =
        this.compose(LoadingDialogTransformer(loadingView, msg))

@CheckReturnValue
fun <T> Observable<T>.loading(loadingView: ILoadingView, msg: String = "加载中"): Observable<T> =
        this.compose(LoadingDialogTransformer(loadingView, msg))

@CheckReturnValue
fun <T> Single<T>.loading(loadingView: ILoadingView, msg: String = "加载中"): Single<T> =
        this.compose(LoadingDialogTransformer(loadingView, msg))

@CheckReturnValue
fun <T> Maybe<T>.loading(loadingView: ILoadingView, msg: String = "加载中"): Maybe<T> =
        this.compose(LoadingDialogTransformer(loadingView, msg))

@CheckReturnValue
fun Completable.loading(loadingView: ILoadingView, msg: String = "加载中"): Completable =
        this.compose(LoadingDialogTransformer<Any>(loadingView, msg))

/************************loading***************/

fun toast(msg: String) {
    CToast.show(msg)
}

fun toast(@IdRes msgRes: Int) {
    CToast.show(msgRes)
}

const val LOG_TAG = "X3-FACE"

fun Any.logV(msg: String, tag: String = LOG_TAG) {
    CLog.v(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logD(msg: String, tag: String =  LOG_TAG) {
    CLog.d(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logI(msg: String, tag: String =  LOG_TAG) {
    CLog.i(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logW(msg: String, tag: String =  LOG_TAG) {
    CLog.w(tag, CLog.msgWithclassName(this, msg))
}

fun Any.logE(msg: String, tag: String =  LOG_TAG) {
    CLog.e(tag, CLog.msgWithclassName(this, msg))
}

fun Context.notifyDialog(title: String, content: String, confirm: (dialog: DialogInterface) -> Boolean = { true }) {
    DialogHelper.notifyDialog(this, title, content, confirm)
}

fun Context.confirmDialog(title: String, content: String, confirm: (dialog: DialogInterface) -> Boolean, cancel:() -> Boolean = {true}) {
    DialogHelper.confirmDialog(this, title, content, confirm, cancel)
}

val globalGson = Gson()

fun <T> String.fromJson(clazz:Class<T>): T?{
    return try {
        globalGson.fromJson(this, clazz)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Any.toJson():String {
    return try {
        globalGson.toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

val Number.dp2px get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()
val Number.px2dp get() = (toInt() / Resources.getSystem().displayMetrics.density).toInt()
val Number.sp2px get() = (toInt() * Resources.getSystem().displayMetrics.scaledDensity).toInt()
val Number.px2sp get() = (toInt() / Resources.getSystem().displayMetrics.scaledDensity).toInt()

