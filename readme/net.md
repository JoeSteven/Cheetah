# 网络模块文档

### 概述

网络模块对`Retrofit` 网络库进行简单封装，主要对 API service 进行初始化，管理多个 API service，提供请求通用 header 及通用参数的注册，提供网络状态的获取等功能 

### 使用

- 定义 api service 接口，这个属于 `Retrofit ` 基本使用，可以参考 `Retrofit` 的官方文档，不再赘述

```kotlin
interface GankService {

    @GET("api/random/data/Android/20")
    fun query():Single<Response>
}
```

- 初始化网络模块，注册通用参数，注册api service。通用参数请在注册service之前注册完毕

```java
// 初始化，service 个数，只用来按照大小创建存储结构，减少内存占用和扩容操作，不会限制service实际注册个数
NetworkCore.init(serviceNumber)
		.registerHeader("header_xxx","value")//注册通用header
		.registerParams("param_xxx","value");// 注册通用参数
```

- 构造api service 实例并注册

```java
// 三个必须调用的接口
NetworkCore.inst()
		.createService(GANK, GankService::class.java)
		.baseUrl("http://gank.io/")
		.register()

// 可选接口
NetworkCore.inst()
		.createService(GANK, GankService::class.java)//service id， 和接口class
		.baseUrl("http://gank.io/")// base url
		.addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 不传默认使用 RxJava2...
		.addConverterFactory(ApiGsonConverterFactory.create(this))// 不传默认使用GsonCo...
		.client(OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS))// 不传默认
  		.headers(headers)// 不传默认使用 NetworkCore 中注册的通用header,null 取消通用header
  		.params(params)// 不传默认使用 NetworkCore 中注册的通用params,null 取消通用params
		.register()// 注册 必须调用！
```

- 解析Response

如果服务端返回的数据结构有自己的状态码或者对真正的数据进行了包裹，类似下面

```json
{
    "code":0,// 状态码
  	"message":"success",// 状态描述信息
  	"data"{...}// 该API真正需要的数据
}
```

可以使用`ApiGsonConverterfactory` 来进行状态码的处理，减少业务层API的data类嵌套

```kotlin
ApiGsonConverterFactory.create(this)

override fun parse(responseJson: String?): String {
	val json = JSONObject(responseJson)
  	val code = json.optInt("code", -1)
	if (code != -1) {
      throw ApiException(, json.optString("message", "error"))
	}
	return json.getString("data")
}
```

- 获取api service 进行网络请求

```Java
NetworkCore.inst()
  .service(API_GANK)// 获取service
  .query()// serivce 的某个请求
```

### 业务层封装

建议业务层实现一个 API 的管理类，用于初始化网络模块，以及给业务层提供api service实例等，例如

```kotlin
object Api : IApiResponseParser {
    private const val GANK = 1

    fun init() {
        NetworkCore.init(1)
                .registerHeader("Header-One", "value-one")
                .registerHeader("Header-Two", "value-two")
                .registerParams("paramOne", "1")
                .registerParams("paramTwo", "2")

        NetworkCore.inst()
                .createService(GANK, GankService::class.java)
                .baseUrl("http://gank.io/")
                .addConverterFactory(ApiGsonConverterFactory.create(this))
                .register()
    }

    fun gank(): GankService {
        return NetworkCore.inst().service<GankService>(GANK)
    }

    override fun parse(responseJson: String?): String {
		val json = JSONObject(responseJson)
  		val code = json.optInt("code", -1)
		if (code != -1) {
      		throw ApiException(, json.optString("message", "error"))
		}
		return json.getString("data")
    }
}
```

