package joey.cheetah.sample.apisample

import android.os.Bundle
import com.joey.cheetah.core.mvp.AbsPresenter

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
class WeatherPresenter(view:IWeatherView) : AbsPresenter<IWeatherView>(view) {
    var weather: WeatherData? = null
    fun fetchData(city:String){
        add(WeatherRepo().query(city)
                .subscribe({t -> showWeather(t?.result?.data) }))
    }

    private fun showWeather(t: WeatherData?) {
        weather = t
        if (isValid && t != null){
            mView.showWeather(t)
            bus().post(WeatherEvent(t.realtime.cityName))
        }
    }

    override fun onSaveData(outState: Bundle?) {

    }

    override fun onRestoredData(savedInstanceState: Bundle?) {
    }
}