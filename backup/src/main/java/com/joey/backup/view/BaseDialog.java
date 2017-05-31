package com.joey.backup.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.ujipin.erp.R;

/**
 * description - 自定义dialog基类
 * <p/>
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public abstract class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setContentView(getLayout());
        initView();
        initData();
        initListener();
    }
    public BaseDialog(Context context) {
        super(context, R.style.DialogNoTitle);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
        setContentView(getLayout());
        initView();
        initData();
        initListener();
    }


    protected abstract int getLayout();
    protected abstract void initView();
    private void initData() {

    }
    protected boolean mCancelable = true;
    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.mCancelable = flag;
    }

    protected void initListener() {
        if(findView(R.id.tv_cancel_dialog)!=null) findView(R.id.tv_cancel_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCancelListener !=null) mCancelListener.onCancel();
                if(mCancelable) dismiss();
            }
        });
        if(findView(R.id.tv_commit_dialog)!=null) findView(R.id.tv_commit_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommit();
                dismiss();
            }
        });

    }
    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(int id){
        return (T) findViewById(id);
    }

    protected OnCommitListener mCommitListener;
    protected OnCancelListener mCancelListener;
    public <T> void  setOnCommitListener(OnCommitListener<T> listener){
        this.mCommitListener = listener;
    }

    public void onCommit(){

    }

    public interface OnCommitListener<T>{
        void onCommit(T t, BaseDialog dialog);
    }

    public interface OnCancelListener{
        void onCancel();
    }
}
