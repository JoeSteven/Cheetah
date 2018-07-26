package cheeta.core;

import android.app.Application;
import android.content.Context;

import cheeta.core.init.InitManager;
import cheeta.core.utils.Global;

/**
 * Description:
 * author:Joey
 * date:2018/7/25
 */
public abstract class CheetahApplication extends Application {
    InitManager mInitManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Global.initContext(this);
        mInitManager = createInitManager();
        mInitManager.addTask();
    }

    protected abstract InitManager createInitManager();
}
