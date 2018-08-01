package joey.cheetah.sample.Api

import cheetah.core.net.NetworkCore
import joey.cheetah.sample.apisample.WeatherService

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
object Api {

    private const val WEATHER = 1


    fun init(){
        NetworkCore.init(1)
                .registerService(WEATHER, "http://op.juhe.cn/", WeatherService::class.java)
    }

    fun weather():WeatherService{
        return NetworkCore.inst().service<WeatherService>(WEATHER)
    }
}