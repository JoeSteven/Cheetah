package com.terminus.iotextension.iot.config;

/**
 * @author rain
 * @date 2019/6/11
 */
public enum NetType {
    /**
     * GPRS
     */
    GPRS(1),

    /**
     * Wifi
     */
    WIFI(2),

    /**
     * 有线网
     */
    Ethernet(4),

    /**
     * lora
     */
    LoRa(8),

    /**
     * zeta
     */
    ZETA(16),

    /**
     * nb
     */
    NB(32),

    /**
     * 3G
     */
    TG(64),

    /**
     * 4G
     */
    FG(128);

    private int value;

    NetType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static NetType fromValue(int value) {
        NetType[] types = NetType.values();
        for (NetType type : types) {
            if (type.value() == value) {
                return type;
            }
        }

        return null;
    }
}
