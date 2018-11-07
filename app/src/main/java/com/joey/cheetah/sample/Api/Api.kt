package com.joey.cheetah.sample.Api

import com.joey.cheetah.core.net.NetworkCore
import com.joey.cheetah.core.net.coverter.ApiGsonConverterFactory
import com.joey.cheetah.core.net.coverter.IApiResponseParser
import com.joey.cheetah.sample.Api.exception.ApiException
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
object Api : IApiResponseParser {
    private const val GANK = 1

    fun init() {
        NetworkCore.init(1)
                .registerHeader("Header-One", "value-one")
                .registerHeader("Header-Two", "value-two")
                .registerParams("paramOne", "1")
                .registerParams("paramTwo", "2")

        NetworkCore.inst()
                .createService(GANK, GankService::class.java)
                .baseUrl("http://gank.io/")
                .register()

        NetworkCore.inst()
                .createService(GANK, GankService::class.java)
                .baseUrl("http://gank.io/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ApiGsonConverterFactory.create(this))
                .client(OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS))
                .register()
    }

    fun gank(): GankService {
        return NetworkCore.inst().service<GankService>(GANK)
    }

    override fun parse(responseJson: String?): String {
        val json = JSONObject(responseJson)
        if (json.optBoolean("error", true)) {
            throw ApiException(0, "api error occurred!")
        }
        return responseJson!!
    }
}