package com.joey.cheetah.sample.apisample

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.joey.cheetah.sample.Api.Api

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
class WeatherRepo{
    fun query(city:String) : Observable<ResponseData<WeatherData>>{
        return Api.weather()
                .queryWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}