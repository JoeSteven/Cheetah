package com.joey.backup.view;

import android.content.Context;
import android.widget.TextView;

import com.ujipin.erp.R;

/**
 * description - 消息提示dialog，不含title
 * <p/>
 * author - Joe.
 * create on 16/7/19.
 * change
 * change on .
 */
public class MessageDialog extends BaseDialog {

    private TextView mTvMsg;
    private TextView mCancel;

    public MessageDialog(Context context) {
        super(context);
    }

    public MessageDialog(Context context, String msg){
        super(context);
        mTvMsg.setText(msg);
    }
    @Override
    protected int getLayout() {
        return R.layout.dialog_message;
    }

    @Override
    protected void initView() {
        mTvMsg = findView(R.id.tv_msg_dialog);
        mCancel = findView(R.id.tv_cancel_dialog);
    }

    public void show(String msg){
        mTvMsg.setText(msg);
        show();
    }

    public void setMsg(String msg){
        mTvMsg.setText(msg);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCommit() {
        if(mCommitListener!=null) mCommitListener.onCommit(null,this);
    }

    public void setCancelName(String cancel){
        mCancel.setText(cancel);
    }
}
