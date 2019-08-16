package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/27
 */
public class DoorEvent extends BaseEvent {
    public final boolean open;

    public DoorEvent(boolean open) {
        this.open = open;
    }
}
