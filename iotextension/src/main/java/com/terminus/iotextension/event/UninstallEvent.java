package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/27
 */
public class UninstallEvent extends BaseEvent {
    public final boolean uninstall;

    public UninstallEvent(boolean uninstall) {
        this.uninstall = uninstall;
    }
}
