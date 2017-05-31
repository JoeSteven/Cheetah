package com.joey.cheetah;


import dagger.Component;

/**
 * description - MVP依赖注入接口
 * 每一个实现了 mvp 的页面
 * 如果view使用了 Presenter 需要在此添加注入
 * Presenter 使用了Model 需要在此添加注入
 * 被注入的 Presenter以及model应该在 AppModule 中添加provide方法
 *
 * author - Joe.
 * create on 16/8/4.
 * change
 * change on .
 */

@Component(modules = AppModule.class)
public interface AppComponent {
//    // 登陆
//    LoginActivity inject(LoginActivity activity);
//    LoginPresenter inject(LoginPresenter presenter);
//    // 首页
//    HomeActivity inject(HomeActivity activity);
//    HomePresenter inject(HomePresenter presenter);
//    //扫描
//    ScanActivity inject(ScanActivity activity);
//    ScanPresenter inject(ScanPresenter presenter);
//    //拣货
//    PickStartFragment inject(PickStartFragment fragment);
//    PickExecuteFragment inject(PickExecuteFragment fragment);
//    PickEndFragment inject(PickEndFragment fragment);
//    PickingPresenter inject(PickingPresenter presenter);
//    //分拣
//    SortStartFragment inject(SortStartFragment fragment);
//    SortExecuteFragment inject(SortExecuteFragment fragment);
//    SortingPresenter inject(SortingPresenter presenter);
//    //盘点
//    CheckScanActivity inject(CheckScanActivity activity);
//    CheckPostActivity inject(CheckPostActivity activity);
//    CheckPresenter inject(CheckPresenter checkPresenter);
//    //移库
//    SwapInventoryActivity inject(SwapInventoryActivity activity);
//    SwapInventoryListFragment inject(SwapInventoryListFragment fragment);
//    SwapInventoryDetailsFragment inject(SwapInventoryDetailsFragment fragment);
//    SwapInventoryPresenter inject(SwapInventoryPresenter swapInventoryPresenter);
//    SwapInventoryDetailsPresenter inject(SwapInventoryDetailsPresenter swapInventoryPresenter);
}
