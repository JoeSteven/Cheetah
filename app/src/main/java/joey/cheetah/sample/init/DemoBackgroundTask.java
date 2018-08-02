package joey.cheetah.sample.init;

import cheetah.core.init.InitTask;
import cheetah.core.utils.CLog;

/**
 * Description:
 * author:Joey
 * date:2018/8/2
 */
public class DemoBackgroundTask extends InitTask {
    @Override
    protected void execute() {
        CLog.d("back_ground", "demo task thread is :" + Thread.currentThread());
    }

    @Override
    public Type type() {
        return Type.BACKGROUND;
    }
}
