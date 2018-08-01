package joey.cheetah.sample.apisample

import cheetah.core.mvp.IView

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
interface IWeatherView : IView{
    fun showWeather(weatherData: WeatherData)
}