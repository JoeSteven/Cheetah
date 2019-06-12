package com.terminus.iotextension.iot;

import com.terminus.iotextension.iot.config.DataType;

import java.util.List;

/**
 * @author rain
 * @date 2019/4/28
 */
public class IoTConstant {
    public static final String RSA_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCb6Fm+ff" +
            "/KOe0kUKcT4n/Uk2+/oEYZKzQhMMGeuB692O/ORZgxOvuZFUsj+e7nt/2tzt08lij3B" +
            "+owZ5qtReYsQXh094qbj1sVEUhhYQe3wzIgN4d5ASdYui/lt7HvsCMkVCoIpwhjv8kWYTZ" +
            "/R7bD2ukPWIqEZ0P4JfyjTYbAaQIDAQAB";

    public static String TCP_ADDRESS = "";
    public static String CATEGORY = "";
    public static String DEVNAME = "";
    public static String TYPE = "";
    public static String MODEL = "";
    public static String HARDWARE_VERSION = "";
    public static String SOFTWARE_VERSION = "";
    public static String FATHER_ID = "0";
    public static String SN = "";
    public static String MAC = "";
    //功能说明:0.支持NFC卡；1.支持指纹；2.支持门锁长开 3.支持IBeacon 5.支持梯控 6.支持一次性密码 7.支持远程开门 9.支持多连接 10.支持新一代门禁 12
    // .支持双模开锁
    public static List<Integer> FUNCTIONS;

    public static byte[] AES_KEY = {};
    public static String DEV_ID = "";
    public static String CLIENT_ID = "";
    public static String USER_NAME = "";
    public static String PASSWORD = "";
    public static String SUB_TOPIC = "";
    public static String PUB_TOPIC = "";
}
