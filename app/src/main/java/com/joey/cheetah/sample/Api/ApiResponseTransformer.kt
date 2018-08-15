package com.joey.cheetah.sample.Api

import com.joey.cheetah.sample.kt.GankData
import com.joey.cheetah.sample.kt.Response
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class ApiResponseTransformer:ObservableTransformer<Response, List<GankData>> {
    override fun apply(upstream: Observable<Response>): Observable<List<GankData>> {
        return upstream.map { t -> t.results }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}