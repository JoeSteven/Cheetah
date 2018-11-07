# 持久化模块

### 1.SharedPreHelper - 轻量k-v存储

用于简化 `SharedPreferences` 的业务层调用，支持链式调用，具体使用如下

- 初始化配置

```java
// 设置应用的默认sp文件名
SharedPrefHelper.setDefaultFileName(string);

// 设置最大缓存的helper数量，采用Lru算法缓存，合理的缓存数量可以提高操作效率
SharedPrefHelper.setMaxHelpersCount(count);
```

- 获取实例

```java
// 获取应用默认的sp文件帮助类
helper = SharedPrefHelper.from(context);

// 获取指定的sp文件帮助类
helper = SharedPrefHelper.from(context, spName);
```

- 写数据

```java
// 可以连续的写数据
SharedPrefHelper.from(this)
		.put(key1, value1)
		.put(key2, value2)
		.apply();// 提交这次写入

// 最终提交数据还有一种方式，直接以apply写数据结尾
SharedPrefHelper.from(this)
		.put(key1, value1)
		.apply(key2, value2);
			
// 只写入组数据的时候，也可以直接以apply的形式写入
SharedPrefHelper.from(this)
		.apply(key1, value1);
```

- 读数据

  与`SharedPreferences` 使用类似，先调用from获取实例，然后 getxxx 方法获取

- 支持移除/清空数据

- 支持查询是否存在数据

- 支持获取`SharedPreferences`

### 2.FileHelper - 文件操作

简化文件操作，API 较多，可以直接在类中查找相关方法

### 3.Room - 数据库框架

建议直接使用 Google 官方推出的数据库框架 Room，需要在应用项目的 build.gradle 中添加如下代码（room的依赖 cheetah 有了，需要为 apt 注解生成代码添加依赖）

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
    kapt "android.arch.persistence.room:compiler:1.1.1"
}
```

Room 可以实现操作接口化，并且查询可以返回 Flowable，便于使用 RxJava 进行操作，除了查询外，其余操作不能返回 RxJava 相关被观察对象，因此做了简单的封装：

#### Java

 ```java
// 使用该方法可以返回一个 Single 对象来进行操作
// 配合 lambda 表达式 可以比较简洁
RxRoomHelper.toSingle(RxRoomOperator<T>);
 ```

#### Kotlin

对 RoomDataBase 进行了扩展，toSingle可以直接使用, 参数为一个表达式，如下

```kotlin
AppDataBase.INSTANCE
		.toSingle { AppDataBase.INSTANCE.userDao().insert(*user) }
		.compose(SchedulersHelper.io_main())
```





