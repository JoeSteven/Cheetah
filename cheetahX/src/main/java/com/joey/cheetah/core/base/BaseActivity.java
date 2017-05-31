package com.joey.cheetah.core.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.joey.cheetah.core.CheetahApplication;
import com.joey.cheetah.uitls.CLog;
import com.joey.cheetah.uitls.CToast;
import com.joey.cheetah.uitls.TUtil;
import com.joey.cheetah.view.DefaultLoadingView;
import com.joey.cheetah.view.ILoadingView;


/**
 * description - Activity 基类.所有view层的类只做UI相关操作，尽可能不出现业务代码
 * 并且不能直接和model进行直接交互
 * <p/>
 * author - Joe.
 * create on 16/7/12.
 * change
 * change on .
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected T mPresenter;
    protected BaseActivity mActivity;
    protected CheetahApplication mApplication;
    private ILoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        CLog.d(getName(),"onCreate is running  getTaskId="+getTaskId() + "isRoot="+isTaskRoot());
        this.mActivity = this;
        this.mApplication = (CheetahApplication) getApplication();
        if (!noPresenter()) {
            mPresenter = TUtil.getT(this, 0);
        }
        initArguments();
        initPresenter();
        initLoading();
        initView();
        initListener();
        if(mPresenter!=null){
            mPresenter.onStart();
        }
    }

    protected void initArguments() {

    }

    /**
     * set activity content view
     */
    protected abstract int getLayout();

    /**
     * 初始化UI相关界面
     */
    protected abstract void initView();

    /**
     * 初始化Presenter 如果没有则不管
     */
    protected abstract void initPresenter();

    /**
     * 监听相关放到这里
     */
    protected void initListener() {
    }

    /**
     * 初始化Loading的dialog
     */
    protected void initLoading() {
        mLoadingView = new DefaultLoadingView(this);
    }


    /**
     * BaseView - 弹吐司
     * 同时关闭loading
     */
    @Override
    public void showToast(String msg) {
        doneLoading();
        CToast.show(msg);
    }

    /**
     * 显示Loading
     */
    @Override
    public void showLoading(String msg) {
        if (mLoadingView == null) return;
        mLoadingView.showLoading(msg);
    }

    /**
     * 关闭Loading
     */
    @Override
    public void doneLoading() {
        if (mLoadingView != null && mLoadingView.isShowing()) {
            mLoadingView.doneLoading();
        }
    }

    /**
     * 当前Activity 类名
     */
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CLog.d(getName(),"onDestroy");
        if (mPresenter != null) mPresenter.onRemove();
        if (mLoadingView != null) {mLoadingView.onDestroy();}
    }

    /**
     * findViewById不需要强转
     */
    @SuppressWarnings("unchecked")
    public <E extends View> E findView(int id) {
        return (E) findViewById(id);
    }

    private long lastTime=0;
    protected String mQuitHint = "再按一次退出";
    protected boolean isTwiceQuit(){
        return false;
    }

    protected boolean noPresenter() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if(!isTwiceQuit()){
            finish();
            return;
        }
        long duration = System.currentTimeMillis()-lastTime;
        lastTime = System.currentTimeMillis();
        if(duration<2000)  {
            finish();
            return;
        }
        CToast.show(mQuitHint);
    }
}
