package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/5/5
 */
public enum OpenType {

    /**
     * 刷卡
     */
    CARD(0),

    /**
     * 一次性密码
     */
    ONCE_PASSWORD(1),

    /**
     * APP远程
     */
    APP_REMOTE(2),

    /**
     * 门内开门
     */
    INDOOR(3),

    /**
     * 人脸识别
     */
    FACE(4),

    /**
     * 指纹识别
     */
    FINGER(5),

    /**
     * 普通密码
     */
    NORMAL_PASSWORD(6),

    /**
     * DTMF
     */
    DTMF(7),

    /**
     * APP蓝牙
     */
    APP_BLUETOOTH(8),

    /**
     * 二维码开门
     */
    CODE(9);

    private int value;

    OpenType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static OpenType fromValue(int value){
        OpenType[] openTypes = OpenType.values();

        for (OpenType openType : openTypes) {
            if (openType.value() == value) {
                return openType;
            }
        }

        return null;
    }
}
