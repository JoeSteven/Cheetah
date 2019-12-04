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
     * 二维码过期
     */
    ERROR_CODE_TIMEOUT(3),

    /**
     * 二维码-用户无权限
     */
    ERROR_CODE_USER(4),

    /**
     * 无效用户
     */
    ERROR_USER(5),

    /**
     * 体重异常
     */
    ERROR_WEIGHT(6),

    /**
     * 人员数量异常
     */
    ERROR_NUM(7),

    /**
     * 人脸比对失败
     */
    ERROR_COMPARE(8),

    /**
     * 人员权限超时
     */
    ERROR_USER_TIMEOUT(9),

    /**
     * 开门失败
     */
    ERROR_DOOR(10);

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
