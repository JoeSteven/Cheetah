package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/5/5
 */
public enum OpenStatus {

    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 失败
     */
    FAIL(1),

    /**
     * 无效二维码
     */
    ERROR_CODE(2),

    /**
     * 无效用户
     */
    ERROR_USER(3);

    private int value;

    OpenStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static OpenStatus fromValue(int value){
        OpenStatus[] openStatuses = OpenStatus.values();

        for (OpenStatus openStatus : openStatuses) {
            if (openStatus.value() == value) {
                return openStatus;
            }
        }

        return null;
    }
}
