package com.joey.cheetah.view;

/**
 * Describe
 * Author Joe
 * created at 17/5/26.
 */

public interface ILoadingView {
    void showLoading(String msg);
    void doneLoading();
    boolean isShowing();
    void onDestroy();
}
