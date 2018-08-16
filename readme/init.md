# 初始化模块

### 概述

初始化模块分为两个部分，应用初始化和模块初始化。

#### 通用初始化

使用 Cheetah 快速开发前，需要在应用 Application 对应的方法中调用`CheetahApplicationInitializer` 的相关方法，目的是为了初始化一些全局参数，及一些必须的配置。调用方式很简单，一共三个接口需要调用：

```Java
public class SampleApplication extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      // 调用 attachBaseContext， 传入一个 InitManager 对象，该对象详细说明见下一节
        CheetahApplicationInitializer.attachBaseContext(this, new SampleInitManger());
    }

    @Override
    public void onCreate() {
      // super.onCreate 前调用一个方法
        CheetahApplicationInitializer.beforeSuperOnCreate();
        super.onCreate();
      // super.onCreate 后调用一个方法
        CheetahApplicationInitializer.afterSuperOnCreate();
    }
}
```



#### 模块初始化

初始化模块的设计准则是将应用启动要执行的初始化代码任务化，而不是全都堆在 `Application.onCreate` 方法中，启动时间变长的时候排查困难，该模块支持以下功能

- 初始化任务逻辑独立，不同的模块初始化代码放在不同的任务中
- 任务支持优先级，按顺序执行
- 任务支持异步执行
- 任务支持耗时统计

##### 1.InitManager

上一节中调用初始化代码时需要传入一个 InitManger 对象，该类即为模块初始化任务的管理类。InitManager 是一个抽象类需要实现一个方法：

```java
public class SampleInitManger extends InitManager {
    @Override
    public void addTask() {
      // 在该方法中调用 add() 来添加要执行的初始化任务，同一优先级类别的任务按照添加的顺序先后执行
        add(new ApiTask());// 例子：API相关初始化任务
        add(new BleTask());// 例子：蓝牙初始化
        add(new ImageTask());
        add(new BackgroundTask());//例子：需要异步执行的任务
    }
  

    /**
     * @param task use to monitor task
     */
    protected void monitorTask(InitTask task) {
		// 任务执行完毕后会回调该方法，可以在该方法中获取每个任务的执行耗时。
      //注意这里只统计主线程任务的执行时间，异步线程任务不会统计
    }
}
```

##### 2.InitTask

该抽象类为初始化任务的顶层基类，所以需要初始化的任务需要继承该类并实现两个方法：

```java
public class ApiTask extends InitTask{
    @Override
    protected void execute() {
      // 任务要执行的代码写在这里
        Api.INSTANCE.init();
    }

    @Override
    public Priority priority() {
      // 任务的优先级
        return Priority.EMERGENCY;
    }
}
```

##### 3.Priority

枚举类，用来标示任务的优先级，同一优先级中任务按顺序执行。任务应该按照最低级别优先的原则进行设置，避免主线程被阻塞太久，导致应用启动耗时太长

```java
public enum Priority {
        EMERGENCY,// 最高优先级，主线程执行，super.onCreate前
        URGENT,// 次高优先级，主线程执行，super.onCreate后，等待EMERGENCY任务执行完毕后执行
        BACKGROUND// 低优先级，子线程执行
    }
```



