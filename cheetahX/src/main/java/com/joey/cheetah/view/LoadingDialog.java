package com.joey.cheetah.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.joey.cheetah.R;


/**
 * description - Loading的dialog
 * <p/>
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public class LoadingDialog extends Dialog {
    private TextView mMsg;

    public LoadingDialog(Context context, int themeId) {
        super(context, themeId);//,R.style.dialog_no_title
        setContentView(getLayout());
        initView();
    }
    public LoadingDialog(Context context){
        super(context, R.style.dialog_no_title);//,R.style.dialog_no_title
        setContentView(getLayout());
        initView();
    }

    protected int getLayout() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return R.layout.cheetah_loading_default;
    }

    protected void initView() {
        mMsg = (TextView) findViewById(R.id.tv_loading_dialog);
    }

    public void setMessage(String msg){
        mMsg.setText(msg);
    }
}
