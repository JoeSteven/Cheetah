package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/22
 */
public class TimeEvent extends BaseEvent {
    public final long time;

    public TimeEvent(long time) {
        this.time = time;
    }
}
