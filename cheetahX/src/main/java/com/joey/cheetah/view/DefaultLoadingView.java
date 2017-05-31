package com.joey.cheetah.view;

import android.content.Context;

import com.joey.cheetah.R;


/**
 * Describe
 * Author Joe
 * created at 17/5/26.
 */

public class DefaultLoadingView implements ILoadingView {
    private LoadingDialog mDialog;

    public DefaultLoadingView(Context context) {
        mDialog = new LoadingDialog(context, R.style.dialog_no_title);
    }

    @Override
    public void showLoading(String msg) {
        mDialog.setMessage(msg);
        mDialog.show();
    }

    @Override
    public void doneLoading() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return mDialog.isShowing();
    }

    @Override
    public void onDestroy() {
        doneLoading();
        mDialog = null;
    }
}
