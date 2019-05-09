package com.terminus.iotextension.iot.config;

/**
 * 进出方向
 */
public enum Direction {

    /**
     * 进
     */
    IN(1),

    /**
     * 出
     */
    OUT(2),

    /**
     * 未知
     */
    ERROR(0);
    
    private int value;

    Direction(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
