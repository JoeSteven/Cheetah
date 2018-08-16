package com.joey.cheetah.sample.Api

import com.joey.cheetah.sample.kt.GankData
import com.joey.cheetah.sample.kt.Response
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class ApiResponseTransformer:SingleTransformer<Response, List<GankData>> {
    override fun apply(upstream: Single<Response>): Single<List<GankData>> {
        return upstream.map { t -> t.results }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}