package com.joey.cheetah.sample.kt
import com.google.gson.annotations.SerializedName


/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */

data class Response(
    @SerializedName("error") val error: Boolean,
    @SerializedName("results") val results: List<GankData>
)

data class GankData(
    @SerializedName("_id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("source") val source: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("used") val used: Boolean,
    @SerializedName("who") val who: String
)