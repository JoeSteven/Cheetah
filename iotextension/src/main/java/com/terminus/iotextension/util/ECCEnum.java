package com.terminus.iotextension.util;

/**
 * description
 * author      kai.mr
 * create      2019-06-05 15:03
 **/
public enum  ECCEnum {
    ALGORITHM("EC"),
    PROVIDER("BC"),
    PUBLIC_KEY("PUBLIC_KEY"),
    PRIVATE_KEY("PRIVATE_KEY");



    private String value;

    ECCEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
