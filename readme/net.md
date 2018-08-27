# 网络模块文档

### 概述

网络模块对`Retrofit` 网络库进行简单封装，主要对 API service 进行初始化，管理多个 API service，提供请求通用 header 及通用参数的注册，提供网络状态的获取等功能 

### 使用

- 定义 api service 接口，这个属于 `Retrofit ` 基本使用，可以参考 `Retrofit` 的官方文档，不再赘述
- 初始化网络模块，注册通用参数，注册api service。通用参数请在注册service之前注册完毕

```java
// 初始化，service 个数，只用来按照大小创建存储结构，减少内存占用和扩容操作，不会限制service实际注册个数
NetworkCore.init(serviceNumber)
		.registerHeader("header_xxx","value")//注册通用header
		.registerParams("param_xxx","value");// 注册通用参数
  // 注册 API service，service的id 用于取出创建成功的service，baserUrl, service的class
  		.registerService(API_GANK, baseUrl, GankService.class);
```

- 获取api service 进行网络请求

```Java
NetworkCore.inst()
  .service(API_GANK)// 获取service
  .XXXX()// serivce 的某个请求
```

建议业务层实现一个 API 的管理类，用于初始化网络模块，以及给业务层提供api service实例等，例如

```kotlin
object Api {

    private const val GANK = 1


    fun init(){
        NetworkCore.init(1)
                .registerService(GANK, "http://gank.io/", GankService::class.java)
    }

    fun gank():GankService{
        return NetworkCore.inst().service<GankService>(GANK)
    }
}
```

