package cheetah.core;

import android.app.Application;
import android.content.Context;

import cheetah.core.init.InitManager;
import cheetah.core.utils.CLog;
import cheetah.core.utils.Global;

/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class CheetahApplication extends Application {
    protected InitManager mInitManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Global.initContext(this);
        mInitManager = createInitManager();
        mInitManager.addTask();
    }

    protected abstract InitManager createInitManager();
}
