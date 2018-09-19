package com.joey.cheetah.core.ktextension

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.joey.cheetah.core.global.Global
import com.joey.cheetah.core.media.glide.GlideApp
import com.joey.cheetah.core.media.glide.GlideRequests
import com.joey.cheetah.core.utils.Jumper
import java.io.File


/**
 * Description: extension for some api
 * author:Joey
 * date:2018/7/30
 */


/*****************************extension for Activity start ***************************************/
fun Activity.jump(target:Class<out Activity>) {
    Jumper.make(this, target)
            .jump()
}

fun Activity.jumpAndFinish(target:Class<out Activity>) {
    Jumper.make(this, target)
            .jump()
    finish()
}

fun Activity.jump(action:String) {
    Jumper.make(this, action)
            .jump()
}

fun Activity.jumpAndFinish(action:String) {
    Jumper.make(this, action)
            .jump()
    finish()
}

fun Activity.jumpForResult(target: Class<out Activity>, requestCode:Int) {
    Jumper.make(this, target)
            .jumpForResult(requestCode)
}

fun Activity.jumpForResult(action:String, requestCode:Int) {
    Jumper.make(this, action)
            .jumpForResult(requestCode)
}

fun Activity.jumpWithParams(target: Class<out Activity>) : Jumper {
    return Jumper.make(this, target)
}

fun Activity.jumpWithParams(action: String) : Jumper {
    return Jumper.make(this, action)
}

fun Activity.jumper(): Jumper {
    return Jumper.make()
}

val Activity.globalContext : Context
    get() = Global.context()

/*****************************extension for Activity end***************************************/

/*****************************extension for Fragment start ***************************************/

fun Fragment.jump(target:Class<out Activity>) {
    this.activity?.jump(target)
}

fun Fragment.jumpAndFinish(target:Class<out Activity>) {
    activity?.apply {
        jump(target)
        finish()
    }
}

fun Fragment.jump(action:String) {
    this.activity?.jump(action)
}

fun Fragment.jumpAndFinish(action: String) {
    activity?.apply {
        jump(action)
        finish()
    }
}

fun Fragment.jumpForResult(target: Class<out Activity>, requestCode:Int) {
    this.activity?.jumpForResult(target, requestCode)
}

fun Fragment.jumpForResult(action:String, requestCode:Int) {
    this.activity?.jumpForResult(action, requestCode)
}

fun Fragment.jumpWithParams(target: Class<out Activity>) : Jumper? {
    return this.activity?.jumpWithParams(target)
}

fun Fragment.jumpWithParams(action: String) : Jumper? {
    return this.activity?.jumpWithParams(action)
}

fun Fragment.globalContext() : Context {
    return Global.context()
}

val Fragment.screenWith
    get() = activity?.screenWidth

val Fragment.screenHeight
    get() = activity?.screenHeight

/*****************************extension for Fragment end***************************************/

/*****************************extension for View start ***************************************/
val View.screenWith
    get() = context.screenWidth

val View.screenHeight
    get() = context.screenHeight
/*****************************extension for View end***************************************/

/*****************************extension for ImageView start***************************************/
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
fun ImageView.glide(): GlideRequests {
    return GlideApp.with(context)
}
/*****************************extension for ImageView end***************************************/

/*****************************extension for EditText start***************************************/
fun EditText.isBlank(): Boolean {
    return text.isBlank()
}
/*****************************extension for EditText end***************************************/