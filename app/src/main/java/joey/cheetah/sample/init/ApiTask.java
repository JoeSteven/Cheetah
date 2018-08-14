package joey.cheetah.sample.init;

import com.joey.cheetah.core.init.InitTask;
import joey.cheetah.sample.Api.Api;

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
public class ApiTask extends InitTask{
    @Override
    protected void execute() {
        Api.INSTANCE.init();
    }

    @Override
    public Type type() {
        return Type.EMERGENCY;
    }
}
