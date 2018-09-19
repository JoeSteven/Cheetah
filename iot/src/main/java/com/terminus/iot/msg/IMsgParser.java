package com.terminus.iot.msg;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */
public interface IMsgParser {
    IotFrame parse(byte[] data);
}
