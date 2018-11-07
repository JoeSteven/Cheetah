

# Cheetah

Cheetah 是一个Android应用快速开发框架，让开发者在开发全新应用的时候更专注于业务层开发。

###  概述

Cheetah 目前划分为两层，基础功能层 (core) 和架构层 (mvp)。

- 基础功能层 - 提供应用开发所需要的基础能力，例如并发，事件分发，应用初始化，网络，多媒体，蓝牙，权限，列表，数据库等等
- 架构层提供了应用开发所需的基础架构，例如MVP，这一层封装好了MVP的架构模式，尽可能的减少业务层的模版代码，同时处理了View 和 Presenter 的双向绑定和生命周期同步等

目前基础功能层都放到 core module 中，后续会将各个基础功能独立为模块，可以根据应用需要去自由组合基础功能。

架构层的设计是基础功能的上层，因此架构层完全可以根据应用的需要去进行更改或者重新设计

### 使用

- 可以将项目clone到本地后，导入需要的module进行开发
- 也可以通过 gradle 直接依赖进行开发

**版本号以最新tag为准**

Core 模块：

```groovy
api 'com.github.JoeSteven.Cheetah:core:[version]'
```

Mvp 模块：

```groovy
api 'com.github.JoeSteven.Cheetah:mvp:[version]'
```



### 模块说明

使用的时候请优先阅读初始化模块的文档，然后再是架构文档

#### 权限声明

```xml
   	<!--蓝牙模块所需权限-->
	<uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
	
	<!--网络模块所需权限-->
    <uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

	<!--持久化模块所需权限-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

#### 架构

[MVP](./readme/mvp.md)

#### 功能

[初始化](./readme/init.md) : init

[并发](./readme/async.md) ：async

[网络](./readme/net.md) : net

[权限](./readme/permission.md) : permission

[工具](./readme/utils.md) : utils

[列表](./readme/list.md) : list

[koltin 扩展](./readme/extension.md) : ktextension...待开发

[WebView](https://github.com/JoeSteven/XWebViewAssistant) : web模块没有直接包含在框架中，可以依赖我的一个封装库[XWebViewAssistant](https://github.com/JoeSteven/XWebViewAssistant)

[持久化](./readme/database.md) : 目前支持`SharedPreferences` 

蓝牙 : 蓝牙模块并没有直接包含在该框架中，可以依赖我的一个封装库[RxBle](https://github.com/JoeSteven/RxBle)

[相机](./readme/camera.md) : camera
=======
#### 混淆文件

项目 release 版本混淆非常有必要，一方面提升代码安全性，大幅降低被反编译后的可读性，另一方面会压缩整个apk的包大小，节省推广及下载成本

在应用主module的 build.gradle 文件中，添加 release 和 debug 构建模式的混淆配置，release开启，debug关闭便于调试和提升编译速度

```groovy
buildTypes {
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }

    debug {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }
}
```

[proguard-rules.pro](./app/proguard-rules.pro) - 可以参考该文件进行混淆配置，对于本框架中未用到的三方库，需要自行添加混淆配置

**业务层中的数据类（Java Bean) 不能被混淆，否则会解析失败，有几种方式来 keep**

- 使用 @keep 注解来保证某个类或者方法不被混淆，使用该方法确保混淆文件中有keep的相关配置

```Kotlin
@Keep
data class Response(
    @SerializedName("error") val error: Boolean,
    @SerializedName("results") val results: List<GankData>
) : Parcelable
```

```Java
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
```

- 在 proguard 文件中添加语句

```Python
-keep class com.joey.cheetah.sample.kt.Response # 每个model都keep
-keep class com.joey.cheetah.sample.model.** { *; } # model放到某个包下，这个包下类都keep
-keep public class * extends com.joey.cheetah.sample.model.BaseModel # 继承自基类 keep 
```

