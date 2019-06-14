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
    UNKNOWN("unknown");

    PersonType(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return value;
    }

    public static PersonType fromValue(String value){
        PersonType[] personTypes = PersonType.values();

        for (PersonType type : personTypes) {
            if (type.value().equals(value)) {
                return type;
            }
        }

        return null;
    }
}
