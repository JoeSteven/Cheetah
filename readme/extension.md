# Kotlin 扩展

### 概述

本模块是对 Android 常用类进行一些功能扩展，简化上层的使用以及减少功能代码侵入性

目前主要有以下扩展，持续补充

### 扩展类

[1.Context](#1.Context 扩展)

[2.Activity](#2.Activity 扩展)

[3.Fragment](#3.Fragment 扩展)

[4.View](#4.View 扩展)

[5.ImageView](#5.ImageView 扩展)

[6.EditText](#6.EditText 扩展)

### 1.Context 扩展

全部 API 可以参考 Kotlin 文件`ContextExtension.kt`

```kotlin
val Context.screenWidth: Int // 屏幕宽度
val Context.screenHeight: Int// 屏幕高度
fun Context.versionCode(pkgName: String = packageName): Int // 项目版本号，默认本项目
fun Context.versionName(pkgName: String = packageName): String // 项目版本名称，默认本项目
fun Context.isAppForeGround(pkgName: String = packageName): Boolean // 项目是否在前台
... 省略
```

所有的`SystemService`，全部 API 可以参考 Kotlin 文件 `ServiceExtension.kt`，部分API展示如下，扩展属性名称与 service 类名对应

```kotlin
val Context.activityManager // Context.ACTIVITY_SERVICE
val Context.displayManager // Context.DISPLAY_SERVICE
val Context.clipboardManager // Context.CLIPBOARD_SERVICE
val Context.telephonyManager // Context.TELEPHONY_SERVICE
... 省略
```

### 2.Activity 扩展

Activity 除了可以使用 Context 扩展函数及属性外，还有一些扩展函数， 参考[页面跳转](./utils.md)

全部 API 请参考Kotlin 文件 `ViewExtension.kt`

```kotlin
val Activity.globalContext // Global.context()

// 跳转到某个页面
fun Activity.jump(target:Class<out Activity>)
fun Activity.jump(action:String) 

// startActivityForResult
fun Activity.jumpForResult(target: Class<out Activity>, requestCode:Int)

// 携带参数，返回一个Jumper，参数放置完毕调用 jump() 或者 jumpForResult 方法
fun Activity.jumpWithParams(target: Class<out Activity>) : Jumper 
```

### 3.Fragment 扩展

扩展函数基本同 Activity ，以及屏幕宽高获取等

### 4.View 扩展

屏幕宽高获取

### 5.ImageView 扩展

```kotlin
/**
* 加载图片，img 为图片资源可以是 url， file， bitmap，resource等
* 支持重设大小
*/
fun ImageView.loadUrl(img: Any?, resizeWidth: Int = -1, resizeHeight: Int = -1)

// 获取 glide 进行加载
fun ImageView.glide(): GlideRequests 
```

### 6.EditText 扩展

```kotlin
fun EditText.isBlank(): Boolean // 判断输入框内容是否为空
```