# 并发模块

### 概述

设计封装并发模块一方面是为了让应用的线程能够统一管理，共用一个线程池，减少业务层频繁创建和销毁线程的开销，同时简化业务层的调用。另外在该模块中封装好相关 Manager 来控制异步回调解绑避免出现内存泄露。该模块设计为三个部分，笔者在并发的底层框架上推荐`RxJava` 因此目前该模块都是基于 `RxJava` 来实现的

- IAsyncExecutor - 异步执行器接口，具体的线程池实现可以根据需要替换，模块中默认实现了 `RxJavaExecutor`，基于 `RxJava` 来实现并发
- IBusStop - 事件分发站，提供了某个事件的分发，及接收事件的注册，默认实现了`RxBus`
- RxJavaManager- 基于`RxJava` 实现的管理类，该对象持有了 `IAsyncExcutor` 及 `IBusStop` 的实现类，并且实现了这两个接口，因此业务层大部分情况都可以只通过该对象来进行相关的并发操作，见[MVP](./mvp.md) 的Prenster相关

### 使用

并发模块有三种功能，分别对应不同的场景，需要知道的是`RxJavaManager` 实现了并发模块的所有接口，因此以下的三种功能 `RxJavaManager` 都可以完成，因此业务层没有特殊需要的情况下持有`RxJavaManager` 对象来进行操作就可以了

**RxJavaManager.clear() 会将以下三种功能的订阅或者任务都移除, 而不需要单独去调用某个接口的clear方法**

#### 1.RxJava的订阅管理

以 MVP 架构为例，假设 M 层都以 `Observable<T>` 的方式来返回数据，P 层需要去订阅这个 `Observable`，那么在完成订阅时候可以使用 `RxJavaManager.add()` 方法将订阅得到的 `Disposable` 加入到管理中，而在 View 层被销毁的时候取消掉所有的异步订阅，避免内存泄露（如果你使用的是Cheetah的 MVP架构，那么在 AbsPresenter 中已经做好了相关处理了

```java
// 添加订阅
manager.add(operator()
  .enable()
  .subscribe(this::success,this::error));

//移除订阅，避免内存泄露
manager.clear();
```

#### 2.事件分发（类似 EventBus)

功能类似于 EventBus，可以在一个类中注册观察某个事件，另外一个类执行完某个逻辑后发送该事件，注册该事件的观察者就可以接受到该事件（发布订阅模式）。支持指定观察线程。

```java
// 注册登陆事件，观察线程为主线程
busStop.subscribe(LoginEvent.class, this::login);  

// 注册登陆事件，观察线程为发布事件的线程
busStop.subscribeOnPostThread(LoginEvent.class, this::login);

// 注册登陆事件，自定义观察线程
busStop.subscribeCustomThread(LoginEvent.class, this::login, Schedulers.io());

// 解除观察
busStop.unsubscribe(LoginEvent.class);
  
// 发布事件
busStop.post(event);

// 移除所有事件观察
busStop.clear();
```

#### 3.执行异步代码

在获取 `IAsyncExecutor`之前需要知道，无论构造多少个`IAsyncExecutor`对象，底层的线程池只有一个，只是任务队列的不同，因此即使频繁构造`IAsyncExecutor` 也不会有线程频繁创建的开销，`IAsyncExecutor` 的构造有两种方式：

```Java
//创建一个新的 IAsyncManager 对象，
AsyncManager.createNew();

// 复用当前已经存在的一个静态 IAsyncManager 对象（全局唯一）
AsyncManager.obtain();
```

具体该用哪一个方法来构造运用该原则来判断即可

**凡是有内存泄露风险的地方均应该使用 createNew 方法来持有一个IAsyncManager 对象，并且在要销毁的时候取消任务，凡是无内存泄露风险，并且异步任务与发起异步任务的对象生命周期无关联的时候则应该使用 obtain 方法**

**注意：`RxJavaManager` 所持有的 IAsyncExecutor 为 createNew 方法构造的，因为`RxJavaManager` 本身是需要被业务层以 new 的方式来构造的，所以并不应该持有全局的执行器**

#####  IAsyncExecutor

```java
// 直接执行一个异步任务，无回调
void execute(AsyncTask task);

// 延迟一段时间执行异步任务，单位毫秒
void execute(AsyncTask task, long delay);

// 执行一个异步任务，并且回调执行结果（无返回值）
void execute(AsyncTask task, IAsyncCallback callback);

// 延迟执行一个异步任务，并且回调执行结果（无返回值）
void execute(AsyncTask task, IAsyncCallback callback, long delay);

// 执行一个异步任务，回调执行结果，有返回值
<T> void execute(AsyncResultTask<T> task, IAsyncResultCallback<T> callback);

// 延迟一个异步任务，回调执行结果，有返回值
<T> void execute(AsyncResultTask<T> task, IAsyncResultCallback<T> callback, long delay);

void clear()// 移除所有异步任务
```