package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/5/5
 */
public enum PersonType {

    /**
     * 用户
     */
    USER("user"),

    /**
     * 黑名单
     */
    BLACK("black"),

    /**
     * 访客
     */
    VISITOR("visitor"),

    /**
     * 陌生人
     */
    UNKNOWN("unkonwn");

    PersonType(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return value;
    }
}
