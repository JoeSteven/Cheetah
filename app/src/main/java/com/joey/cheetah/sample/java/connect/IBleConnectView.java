package com.joey.cheetah.sample.java.connect;

import com.joey.cheetah.mvp.IView;

import java.util.List;

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
public interface IBleConnectView extends IView {
    void showConnect();

    void showConnecting();

    void showDisconnect();

    void showConnectList(List<?> services);

    void switchNotifyOrIndicate(String text, boolean show);

    void showMessage(String message);
}
