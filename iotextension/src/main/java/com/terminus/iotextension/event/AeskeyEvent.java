package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/6
 */
public class AeskeyEvent extends BaseEvent{

    public final byte[] key;

    public AeskeyEvent(byte[] key) {
        this.key = key;
    }
}
