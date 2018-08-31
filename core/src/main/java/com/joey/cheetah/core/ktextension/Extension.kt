package com.joey.cheetah.core.ktextension

import android.net.Uri
import android.widget.ImageView
import com.joey.cheetah.core.media.glide.GlideApp
import com.joey.cheetah.core.media.glide.GlideRequests
import java.io.File


/**
 * Description: extension for some api
 * author:Joey
 * date:2018/7/30
 */

/**
 * extension for ImageView to load picture quickly
 */
fun ImageView.loadUrl(img: Any?, resizeWidth: Int = -1, resizeHeight: Int = -1) {
    var requests = GlideApp.with(context)
    var request = when (img) {
        is String -> requests.load(img)
        is File -> requests.load(img)
        is Int -> requests.load(img)
        is Uri -> requests.load(img)
        else -> requests.load(img)
    }
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