package com.joey.cheetah.core.base;


import com.joey.cheetah.undo.AsyncManager;
import com.joey.cheetah.uitls.TUtil;



/**
 * description - presenter的基类
 * 如果一个presenter用到了多个model 必须注释说明
 *
 * author - Joe.
 * create on 16/7/12.
 * change
 * change on .
 */
public abstract class BasePresenter<T extends BaseView,E extends BaseModel> {
    protected T mView;
    protected E mModel;

    protected AsyncManager mAsyncManager;

    public void onStart(){}
    protected void initModel(){

    }
    protected abstract AsyncManager getAsyncManager();

    public BasePresenter(){
        mAsyncManager = getAsyncManager();
        mModel = TUtil.getT(this, 1);
        initModel();
    }

    /**
     * 初始化presenter 必须调用
     * params view 实现了BaseView的类
     */
    public void setView(T view){
        this.mView = view;
    }

    /**在Activity被销毁时调用,移除异步任务*/
    public void onRemove(){
        if(mAsyncManager !=null) mAsyncManager.onDestroy();
    }

    /**Log的时候需要识别*/
    public String getName(){
        return getClass().getSimpleName();
    }
}
