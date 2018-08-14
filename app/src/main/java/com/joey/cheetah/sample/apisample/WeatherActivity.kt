package com.joey.cheetah.sample.apisample

import android.text.TextUtils
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.mvp.auto.Presenter
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
class WeatherActivity : AbsActivity(), IWeatherView {
    override fun showWeather(weatherData: WeatherData) {
        tvCity.text = weatherData.realtime.cityName
        tvTemper.text = weatherData.realtime.weather.temperature
        tvTime.text = weatherData.realtime.date
    }

    @Presenter
    private val mPresenter = WeatherPresenter(this)
    override fun initLayout(): Int {
        return R.layout.activity_weather
    }

    override fun initView() {
        btQuery.setOnClickListener({
            if (!TextUtils.isEmpty(etQuery.text))
                mPresenter.fetchData(etQuery.text.toString())
        })
    }

    override fun initPresenter(){

    }

    override fun initData() {
        mPresenter.fetchData("重庆")
    }

}