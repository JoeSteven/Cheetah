package com.joey.cheetah.sample.kt
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
@Parcelize
@Keep
data class Response(
    @SerializedName("error") val error: Boolean,
    @SerializedName("results") val results: List<GankData>
) : Parcelable

@Parcelize
@Keep
data class GankData(
    @SerializedName("_id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("images") val images: List<String>?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("source") val source: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("used") val used: Boolean,
    @SerializedName("who") val who: String?
) : Parcelable