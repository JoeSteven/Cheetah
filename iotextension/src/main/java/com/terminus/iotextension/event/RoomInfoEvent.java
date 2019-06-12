package com.terminus.iotextension.event;

import com.terminus.iotextension.mqtt.protobuf.TSLIOTDataSync;

import java.util.List;

/**
 * @author rain
 * @date 2019/6/11
 */
public class RoomInfoEvent extends BaseEvent {
    //设备ID
    public final String devId;
    //本次更新的最大版本号
    public final long version;
    //剩余待更新的数量:-1-通知更新，0-更新完成，>0-剩余待更新的数量
    public final int more;
    //更新的人员信息列表
    public final List<TSLIOTDataSync.TSLIOTRoomInfoItem> items;

    public RoomInfoEvent(String devId, long version, int more, List<TSLIOTDataSync.TSLIOTRoomInfoItem> items) {
        this.devId = devId;
        this.version = version;
        this.more = more;
        this.items = items;
    }
}
