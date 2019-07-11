package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/6/21
 */
public enum PersonError {

    /**
     * 重新处理成功
     */
    SUCCESS("success"),

    /**
     * 图片服务器无响应
     */
    NORESPONSE("server_no_response"),

    /**
     * 没有照片
     */
    NOPIC("no_picture"),

    /**
     * 建模失败
     */
    MODEL("model_fail"),

    /**
     * 图片内容为空
     */
    EMPTY("empty_picture"),

    /**
     * 格式错误
     */
    FORMAT("error_format"),

    /**
     * 检测不到人脸
     */
    NOFACE("no_face"),

    /**
     * 人脸大小不合格
     */
    SIZE("error_size"),

    /**
     * 人脸角度不合格
     */
    ANGLE("error_angle"),
    /**
     * 其他
     */
    OTHER("error_other");

    PersonError(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return value;
    }

    public static PersonError fromValue(String value){
        PersonError[] personTypes = PersonError.values();

        for (PersonError type : personTypes) {
            if (type.value().equals(value)) {
                return type;
            }
        }

        return null;
    }
}
