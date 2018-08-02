package joey.cheetah.sample;

import cheetah.core.CheetahApplication;
import cheetah.core.init.InitManager;
import joey.cheetah.sample.init.ApiTask;
import joey.cheetah.sample.init.DemoBackgroundTask;
import joey.cheetah.sample.init.ImageTask;

/**
 * Description:
 * author:Joey
 * date:2018/7/31
 */
public class SampleApplication extends CheetahApplication{
    @Override
    public void onCreate() {
        mInitManager.beforeOnCreate();
        super.onCreate();
        mInitManager.afterOnCreate();
    }

    @Override
    protected InitManager createInitManager() {
        return new InitManager() {
            @Override
            public void addTask() {
                add(new ApiTask());
                add(new ImageTask());
                add(new DemoBackgroundTask());
            }
        };
    }
}
