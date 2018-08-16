# MVP 架构层

### 概述

以MVP为架构设计思路，实现以下功能

- V 和 P 的双向绑定自动化
- V 和 P 的生命周期同步，P 层自动解绑异步事件防止内存泄露
- 自动绑定的前提下，实现P层复用，避免P层过度臃肿
- 同步V层的页面回收和重建信息到P层，由P层实现数据保存和恢复

### 关键类

#### View

- 接口：`IView`  V层的顶层接口，所有的View接口都应该继承该接口，该类继承了LifecycleOwner接口用于同步生命周期，还定义了一个接口方法 toast 需实现类来实现
- 抽象类：
  - `AbsActivity`: 应用Activity的顶层基类，实现了 `IView `接口，并且实现了 `LifecycleOwner` 接口，按照执行顺序封装了一些流程抽象方法，子类应尽量在相关方法中去执行相关代码
  - `AbsFragment`:应用Fragment的顶层基类，与 `AbsActivity` 一样。
  - `AbsFragmentActivity` : 持有Fragment的 Activity 的顶层基类，该类封装了 Fragment 的相关操作方法，并且强制子类去实现创建，恢复实例，attach 实例这三个方法，把三个逻辑独立开，避免重复创建，重复attach等bug。支持一个页面同时展示多个Fragment的管理。

####Presenter

- 抽象类：`AbsPresenter` 应用 Presenter 的顶层基类，一个 Presenter 只应该对应一个 View 接口，以泛型的形式声明绑定，该类实现了 LifecycleObserver 用于同步View的生命周期，持有了一些P层常用的对象进行并发，事件分发注册等操作
- 注解：`@Presenter` ，该注解用于绑定 View 和 Presenter ，在View层中声明Presenter时加上该注解，则会自动绑定，绑定的逻辑由 `IPresenterProvider` 提供，业务层可以自己实现

### Model

Model 层主要为数据仓库，一般只需要 Presenter 单向获取数据，因此没有太多的层级交互逻辑，故不进行封装

### 用法

#### View

#####1.继承自 `AbsActivity` ，以下方法均是按顺序执行

```java
    @Presenter
    SampleOnePresenter presenterOne;// 声明 Presenter,注意需要实现该 Presenter 绑定的 View 接口
	// 不需要手动创建实例
	@Presenter
	SampleTwoPresenter presenterTwo;// 一个 Activity 持有对个 Presenter 时，也只需要正常声明
	
	@Presenter
	val presenterOne = SampleOnePresenter(this)// kotlin 的声明方式
      
	@Presenter
	lateinit var presenterOne:SampleOnePresenter// kotlin 的声明方式
      
    @Override
    protected int initLayout() {
        return R.layout.activity_main;// 布局文件
    }

    @Override
    protected void initArguments(@NonNull Intent intent) {
        super.initArguments(intent);// 获取intent中的数据
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();// 初始化 Presenter，例如需要给 Presenter 设置某些参数
      // 注意这里不是创建 Presenter 实例的地方
      	presenterOne.setXXX(xxx);
    }

    @Override
    protected void initView() {
      // 初始化 UI， 也可以直接使用butterKnife，基类已经进行绑定和解绑了
        findViewById(R.id.bt_java).setOnClickListener(v -> {
            Jumper.make(this, BleScanActivity.class).jump();});

        findViewById(R.id.bt_kt).setOnClickListener(v -> {
            Jumper.make(this, GankActivity.class).jump();});
    }

    @Override
    protected void initData() {
      // 初始化数据
        presenterOne.queryData();
      	presenterTwo.queryData();
    }
```

**注意：initData方法默认只有当一个Activity新创建的时候才会调用，而页面重建的时候并不会调用该方法，而是会去调用绑定的Presenter的 onRestoredData 方法。因为重建的时候认为数据应该被恢复而不是重新获取，`AbsFragment` 同理**



#####2.继承自`AbsFragmentActivity`， 除了 `AbsActivity` 中的相关方法外，还必须实现以下方法

```java
    @Override
    protected void createFragment() {
      // 页面新建时，创建 Fragment 实例
        fragment = new SampleFragment();
    }

    @Override
    protected void restoreFragment(Bundle savedInstanceState) {
      // 页面重建时，优先通过 fragmentManager 获取实例，避免重复创建
		fragment = fragmentManager().findFragmentByTag("sample_frag_one");
      	if（fragment == null) {
            fragment = new SampleFragment();
        }
    }

    @Override
    protected void attachFragment() {
      // 添加到页面上
		addFragment(fragment, R.id.fl_content, "sample_frag_one");
    }
	
	// 假设点击按钮切换fragment
	private void clickSwitch() {
      // 调用 switchFragment 方法切换fragment
        switchFragment(targetFragment, R.id.fl_content, "sample_frag_two")
    }
```

以上为常规的用法，另外`AbsFragment` 提供了如下方法来操作 Fragment:

```Java
FragmentManager fragmentManager() // 获取FragmentManager
Fragment currentFragment(@IdRes int contentId) // 获取某一个布局当前的Fragment
//添加fragment
void addFragment(Fragment targetFragment, @IdRes int contentId, String tag)
//添加fragment到后退栈
void addFragmentToStack(Fragment targetFragment, @IdRes int contentId, String tag)
// 替换Fragment
void replaceFragment(Fragment targetFragment, @IdRes int contentId, String tag)
// 切换Fragment
void switchFragment(Fragment targetFragment, @IdRes int contentId, String tag)
```



##### 3.继承自`AbsFragment`，基本与 `AbsActivity` 用法相同，也绑定了ButterKnife，可以直接使用。initData方法调用逻辑与`AbsActivity`一致

```java
    @Override
    protected int initLayout() {
        return 0;
    }
    
    @Override
    protected void initArguments(Bundle arguments) {}

    @Override
    protected void initPresenter() {}     

    @Override
    protected void initView() {}

    @Override
    protected void initData() {}
```



**注意：除以上三种已经实现好的View以外，若想使用其他类作为View层，一定要实现LifecycleOwner，否则会导致生命周期同步失败，具体实现参考Google官方文档**



#### Presenter

##### 继承自`AbsPresenter`，需要指定绑定的 View 接口类型，

##### 并发模块使用RxJava，所以在进行订阅的时候需要调用add() 方法来管理订阅

##### 理论上每个Presenter 都应该实现数据的保存和恢复，因此强制子类实现这两个方法

```Java
public class GankPresenter extends AbsPresenter<IGankView>{

    public GankPresenter(IGankView view) {
        super(view);
    }
  
  	public void query(){
      // 订阅了一个仓库，add进订阅管理，destroy的时候会自动解除订阅
        add(repository.query()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView.loading() }
                .doOnSuccess { mView.stopLoading() }
                .doOnError { mView.stopLoading() }
                .subscribe({ t -> mView.showContent(t) },
                        { e -> mView.toast("load data failed! $e") }))
    }
  
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
      // 页面onStart的时候订阅一个事件，用法同EventBus
        bus().subscribe(LoginEvent.class, t -> mView.login());
    }

    @Override
    public void onSaveData(Bundle outState) {
        // 保存数据
    }

    @Override
    public void onRestoredData(Bundle savedInstanceState) {
		// 恢复数据
    }
}
```

`AbsPresenter` 的API

```Java
void add(Disposable disposable)// 管理订阅
IAsyncExecutor async()// 获取异步执行器
RxJavaManager manager() // 获取RxJavaManager
IBusStop bus()// 获取事件分发处理器
boolean isValid()// 判断当前view是否为空
```

并发相关的API，查看[并发](./async.md)