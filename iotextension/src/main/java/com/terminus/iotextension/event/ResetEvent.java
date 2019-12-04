package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/27
 */
public class ResetEvent extends BaseEvent {
    public final boolean reset;

    public ResetEvent(boolean reset) {
        this.reset = reset;
    }
}
