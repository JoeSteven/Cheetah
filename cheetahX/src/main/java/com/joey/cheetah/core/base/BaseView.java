package com.joey.cheetah.core.base;

/**
 * description - View层的最底层接口，提供方法给P层刷新UI
 * author - Joe.
 * create on 16/7/12.
 * change
 * change on .
 */
public interface BaseView {
    //弹吐司
    void showToast(String msg);

    void showLoading(String msg);

    void doneLoading();
}
