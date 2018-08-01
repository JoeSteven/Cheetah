package joey.cheetah.sample.apisample
import com.google.gson.annotations.SerializedName


/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */

data class WeatherData(
    @SerializedName("realtime") val realtime: Realtime
)

data class Realtime(
    @SerializedName("city_code") val cityCode: String,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("week") val week: String,
    @SerializedName("weather") val weather: Weather,
    @SerializedName("humidity") val humidity: String,
    @SerializedName("info") val info: String,
    @SerializedName("img") val img: String
)

data class Weather(
        @SerializedName("temperature") val temperature: String
)

