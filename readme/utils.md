# 工具模块

### 概述

针对不同的功能封装了一系列的工具类，让业务层专注业务开发

[页面跳转](#页面跳转)		[Bundle打包](#Bundle打包)	 [日志打印](#日志打印) 	[资源获取](#资源获取) 	[UI工具](#UI工具)   持续开发中...

文件工具 - 文件工具包含在持久化模块，可以参考该模块文档[持久化](./database.md)

### 页面跳转

快速实现`Activity` 间跳转，`Fragment` 数据传递等

`Jumper` - Activity 跳转类

```java
Jumper.make(this, GankActivity.class)// 显示跳转，必须
		.setAction(action)// 可选
		.addCategory(category)// 可选
		.addFlag(flag)// 可选
		.putString("xxx", "xxx")// 传递参数
		//put... 传递各种参数
		.jump();// 执行，必须
		
Jumper.make(this, action)// 隐式跳转
	//...
	.jump(); 

Jumper.make();// 构造一个 Jumper

intent();// 获取intent
```



### Bundle打包

`BunderMaker` - 数据包装类，快速生成Bundle 适用于打包数据（存储，传递，恢复等）

```java
Bundle bunle = BundleMaker.start()//必须
                .putString(key, value)// 传数据
                .putByte(key, value)
                .putInt(key, value)
  				//...支持bundle所能支持的所有数据
                .make();
```



### 日志打印

`CLog` - 用法同`Log`，只会在debug模式下打印日志

```java
forceLog(true)// 强制打开日志，release包需要调试的时候，可以通过埋开发者入口，或者后端控制的方式来调用该接口
```



### 资源获取

`ResGetter` - 通过资源id 获取资源实例等功能，用法简单，静态调用



### UI 工具

`UIUtil` - 提供UI的相关的功能，如dp和px互转等，持续开发中...

































### 