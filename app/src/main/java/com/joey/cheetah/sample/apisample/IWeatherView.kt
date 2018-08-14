package com.joey.cheetah.sample.apisample

import com.joey.cheetah.mvp.IView

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
interface IWeatherView : IView {
    fun showWeather(weatherData: WeatherData)
}