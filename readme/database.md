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



