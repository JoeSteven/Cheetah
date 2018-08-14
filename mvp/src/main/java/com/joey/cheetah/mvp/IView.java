package com.joey.cheetah.mvp;

import android.arch.lifecycle.LifecycleOwner;

/**
 * Description: View in MVP, to control user interface
 * author:Joey
 * date:2018/7/25
 */
public interface IView extends LifecycleOwner{
    void toast(String msg);

    void toast(int stringRes);
}
