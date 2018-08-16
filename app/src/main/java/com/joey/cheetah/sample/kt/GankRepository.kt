package com.joey.cheetah.sample.kt

import com.joey.cheetah.sample.Api.Api
import com.joey.cheetah.sample.Api.ApiResponseTransformer
import io.reactivex.Single

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
}