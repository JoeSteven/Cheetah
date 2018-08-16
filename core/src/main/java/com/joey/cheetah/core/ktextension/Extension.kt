package com.joey.cheetah.core.ktextension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.joey.cheetah.core.media.glide.GlideApp
import com.joey.cheetah.core.media.glide.GlideRequest
import com.joey.cheetah.core.media.glide.GlideRequests
import io.reactivex.Observable
import java.io.File


/**
 * Description: extension for some api
 * author:Joey
 * date:2018/7/30
 */

/**
 * extension for ImageView to load picture quickly
 */
fun ImageView.loadUrl(img: String?, resizeWidth: Int = -1, resizeHeight: Int = -1) {
    var requests = GlideApp.with(context)
    var request: GlideRequest<Drawable> = requests.load(img)
//    request = when (img) {
//        is String -> requests.load(img)
//        is File -> requests.load(img)
//        is Int -> requests.load(img)
//        else -> requests.load(img)
//    }
    request.centerCrop()
    if (resizeWidth != -1 && resizeHeight != -1) {
        request.override(resizeWidth, resizeHeight)
    }
    request.into(this)
}

/**
 * extension for ImageView to load picture with custom params
 */
fun ImageView.with(): GlideRequests {
    return GlideApp.with(context)
}