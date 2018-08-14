package com.joey.cheetah.sample.apisample

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
interface WeatherService{
    @GET("http://op.juhe.cn/onebox/weather/query?key=f009177615ba3d5764b160e09d6eebbb")
    fun queryWeather(@Query("cityname") city:String): Observable<ResponseData<WeatherData>>
}