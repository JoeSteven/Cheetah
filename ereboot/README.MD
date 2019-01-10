# 异常捕获及重启模块

### 概述

该库的主要作用是在为一些需要长时间运行的应用提供crash后自动重启的功能。支持以下功能

- 异常重启
- 重启条件过滤
- 异常发生状态回调
- 异常日志
- debug模式（debug模式下不重启，直接crash，以免在调试期间漏掉一些crash）

### 依赖

```groovy
api 'com.terminus:ereboot:version'
```

###  使用

在应用启动时初始化

```kotlin
ERebootUncaughtExceptionHandler.init(ERebootConfig)// 初始化配置
ERebootConfig.with(application, MainActivity::class.java)//传入要异常发生时要重启的activity
			.setDebugMode(true, BuildConfig.DEBUG)// 开启调试模式，debug包直接crash不重启
			.setOnExceptionOccurListener(listener)// 设置异常发生时的状态回调
			.setLogFile(fileName, dir)// 设置日志文件名字及存储路径
			.setLogWriter(writer)// 设置日志文件写入器
			.create()// 创建配置
```

除了 with() 方法必传外，其余参数都有默认值，默认值可以在注释中查看

- 回调接口

```kotlin
interface OnExceptionOccurListener{
  //当异常发生时马上回调该方法，可以在该回调中进行一些重要数据的保存, 注意要持久化存储，因为进程将被重启，内存中的数据不会被保存下来
	fun onErrorOccurred(e: Throwable, t: Thread)

  // 是否需要重启，如果某些异常不需要捕获直接crash则在该方法中返回false，默认为true
	fun shouldReboot(e: Throwable,t: Thread):Boolean

  //开始重启应用回调该方法，可以在该方法中去记录当前是重启状态，如果应用重启后有相应逻辑的话。同样注意也需要持久话存储
	fun onReboot()
}

interface ExceptionLogWriter {
  // 默认会写入到文件中，如果需要自己处理，例如上报服务器，可以在该方法中处理
	fun write(log:String, logFilePath:String, t: Thread, e:Throwable)
}
```

