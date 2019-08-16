package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/7/22
 */
public enum UpdateType {

    /**
     * 接收到升级任务并识别
     */
    START(1),

    /*
     * 开始下载
     */
    DOWNING(2),

    /**
     * 下载完成
     */
    DOWNED(3),

    /**
     * 下载状态识别
     */
    DOWONSTATUS(4),

    /**
     * 开始安装
     */
    INSTALL(5),

    /**
     * 安装进度
     */
    INSTALLING(6),

    /**
     * 安装完成
     */
    INSTALLED(7),

    /**
     * 开始回滚
     */
    BACK(8),

    /**
     * 回滚完成
     */
    BACKED(9);


    UpdateType(int value) {
        this.value = value;
    }

    private int value;

    public int value() {
        return value;
    }

    public static UpdateType fromValue(int value) {
        UpdateType[] values = UpdateType.values();

        for (UpdateType type : values) {
            if (type.value() == value) {
                return type;
            }
        }

        return null;
    }


    public enum ErrorCode {

        /**
         * 成功
         */
        SUCCESS(0),

        /**
         * 升级任务不适用改版本
         */
        VERSION(-1),

        /**
         * 网络原因下载出错
         */
        DOWN(-2),

        /**
         * 资源包找不到
         */
        RES(-3),

        /**
         * 资源包解析出错
         */
        ZIP(-4),

        /**
         * 安装失败
         */
        INSTALL(-5),

        /**
         * 回滚失败
         */
        BACK(-6);

        private int code;

        ErrorCode(int code) {
            this.code = code;
        }

        public int value() {
            return code;
        }

        public static ErrorCode fromValue(int value) {
            ErrorCode[] values = ErrorCode.values();

            for (ErrorCode code : values) {
                if (code.value() == value) {
                    return code;
                }
            }

            return null;
        }

    }
}
