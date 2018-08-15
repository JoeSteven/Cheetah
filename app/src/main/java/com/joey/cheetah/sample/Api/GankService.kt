package com.joey.cheetah.sample.Api

import com.joey.cheetah.sample.kt.Response
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
interface GankService {

    @GET("api/random/data/Android/20")
    fun queryAndroid():Observable<Response>

    @GET("api/random/data/福利/20")
    fun querySurprise():Observable<Response>
}