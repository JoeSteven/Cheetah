package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/5/22
 */
public class ServerEvent extends BaseEvent {

    public final boolean network;
    public final String zimgUrl;
    public final String httpUrl;

    public ServerEvent(boolean network, String zimgUrl, String httpUrl) {
        this.network = network;
        this.zimgUrl = zimgUrl;
        this.httpUrl = httpUrl;
    }
}
