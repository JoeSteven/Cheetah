package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/13
 */
public class CodeEvent extends BaseEvent {

    public final String aesKey;
    public final String rsaKey;
    public final int duration;

    public CodeEvent(String aesKey, String rsaKey, int duration) {
        this.aesKey = aesKey;
        this.rsaKey = rsaKey;
        this.duration = duration;
    }
}
