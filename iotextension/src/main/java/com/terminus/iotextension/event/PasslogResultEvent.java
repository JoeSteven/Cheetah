package com.terminus.iotextension.event;

import com.terminus.iotextension.mqtt.protobuf.TSLIOTBusinessLog;

import java.util.List;

/**
 * @author rain
 * @date 2019/6/04
 */
public class PasslogResultEvent extends BaseEvent {
    //上传信息列表
    public final List<TSLIOTBusinessLog.TSLIOTLogCommonResult> items;
    //日志总数
    public final int count;

    public PasslogResultEvent(List<TSLIOTBusinessLog.TSLIOTLogCommonResult> items, int count) {
        this.items = items;
        this.count = count;
    }
}
