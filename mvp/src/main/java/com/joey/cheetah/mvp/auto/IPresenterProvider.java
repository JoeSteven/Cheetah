package com.joey.cheetah.mvp.auto;

import android.os.Bundle;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public interface IPresenterProvider {
    void create(Object host);

    void onRestore(Bundle data);

    void onSave(Bundle data);
}
