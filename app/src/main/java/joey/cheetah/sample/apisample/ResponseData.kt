package joey.cheetah.sample.apisample
import com.google.gson.annotations.SerializedName



/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */

data class ResponseData<T>(
    @SerializedName("reason") val reason: String,
    @SerializedName("result") val result: Result<T>,
    @SerializedName("error_code") val errorCode: Int
)

data class Result<T>(
    @SerializedName("data") val data: T
)