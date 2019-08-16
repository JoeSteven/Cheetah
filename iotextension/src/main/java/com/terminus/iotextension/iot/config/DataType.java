package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/5/5
 */
public enum DataType {

    /**
     * user+visitor
     */
    PERSON("person"),

    /**
     * user
     */
    USER("user"),

    /**
     * visitor
     */
    VISITOR("visitor"),

    /**
     * 黑名单
     */
    BLACK("black"),

    /**
     * 发卡数据
     */
    CARD("make_card"),

    /**
     * 房间信息
     */
    ROOM("room_info");

    private String value;

    DataType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DataType fromValue(String value){
        DataType[] datas = DataType.values();

        for (DataType data : datas) {
            if (data.value().equals(value)) {
                return data;
            }
        }

        return null;
    }
}
