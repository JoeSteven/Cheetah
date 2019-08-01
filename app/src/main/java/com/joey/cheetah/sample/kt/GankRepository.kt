package com.joey.cheetah.sample.kt

import com.joey.cheetah.core.ktextension.logD
import com.joey.cheetah.sample.Api.Api
import com.joey.cheetah.sample.Api.ApiResponseTransformer
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankRepository {
    fun queryAndroid():Single<List<GankData>> {
        return Api.gank().queryAndroid()
                .compose(ApiResponseTransformer())
    }

    fun querySurprise():Single<List<GankData>> {
        return Api.gank().querySurprise()
                .compose(ApiResponseTransformer())
    }

    fun demoTest() {
        Observable.just("api/random/data/Android/20?paramsOrigin=3",
                "api/random/data/Android/20?paramsOrigin=4",
                "api/random/data/Android/20?paramsOrigin=5")
                .flatMap {
                    val url = it
                    Api.gank()
                            .demoTest(it)
                            .doOnSubscribe { logD("测试","执行:$url, 执行线程:${Thread.currentThread()}") }
                            .compose(ApiResponseTransformer())
                            .map { url }
                            .toObservable()
                }
                .concatMap { Observable.just(it) }
                .subscribeOn(Schedulers.computation())
                .subscribe {
                    logD("测试", "结果：$it")
                }
    }
}