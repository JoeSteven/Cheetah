package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/5/5
 */
public enum DevStatus {

    /**
     * 关闭
     */
    CLOSED(0),

    /**
     * 开启
     */
    OPEN(1),

    /**
     * 异常
     */
    ERROR(2);

    private int value;

    DevStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DevStatus fromValue(int value){
        DevStatus[] devs = DevStatus.values();

        for (DevStatus dev : devs) {
            if (dev.value() == value) {
                return dev;
            }
        }

        return null;
    }
}
