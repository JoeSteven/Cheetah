package com.joey.cheetah.core.ktextension

import com.joey.cheetah.core.ui.loading.ILoadingView
import io.reactivex.*
import org.reactivestreams.Publisher

class LoadingDialogTransformer<T> @JvmOverloads constructor(private val fragment: ILoadingView, private val msg: String = "") : LoadingTransformer<T>(
        onShow = { fragment.startLoading(msg) },
        onDismiss = { fragment.stopLoading() }
)

open class LoadingTransformer<T>(val onShow: () -> Unit, val onDismiss: () -> Unit) : RxTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream.doOnSubscribe { onShow() }.doFinally { onDismiss() }
    override fun apply(upstream: Flowable<T>): Publisher<T> = upstream.doOnSubscribe { onShow() }.doFinally { onDismiss() }
    override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream.doOnSubscribe { onShow() }.doFinally { onDismiss() }
    override fun apply(upstream: Single<T>): SingleSource<T> = upstream.doOnSubscribe { onShow() }.doFinally { onDismiss() }
    override fun apply(upstream: Completable): CompletableSource = upstream.doOnSubscribe { onShow() }.doFinally { onDismiss() }
}
