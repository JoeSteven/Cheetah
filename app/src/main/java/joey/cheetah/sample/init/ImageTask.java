package joey.cheetah.sample.init;

import cheetah.core.init.InitTask;
import cheetah.core.media.ImageHelper;
import cheetah.core.utils.CLog;
import cheetah.core.utils.Global;

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
public class ImageTask extends InitTask {
    @Override
    protected void execute() {
        CLog.d("image_init", "init");
        ImageHelper.init(Global.context());
    }

    @Override
    public InitTask.Type type() {
        return Type.EMERGENCY;
    }
}
