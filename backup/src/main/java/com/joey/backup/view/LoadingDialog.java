package com.joey.backup.view;

import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.ujipin.erp.R;

/**
 * description - Loading的dialog
 * <p/>
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public class LoadingDialog extends BaseDialog {
    private TextView mMsg;

    public LoadingDialog(Context context, String msg) {
        super(context);//,R.style.dialog_no_title
        mMsg.setText(msg);
    }
    public LoadingDialog(Context context){
        super(context);//,R.style.dialog_no_title
        mMsg.setText("");
    }

    @Override
    protected int getLayout() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return R.layout.dialog_loading;
    }

    protected void initView() {
        mMsg = (TextView) findViewById(R.id.tv_loading_dialog);
    }

    public void setMessage(String msg){
        mMsg.setText(msg);
    }
}
