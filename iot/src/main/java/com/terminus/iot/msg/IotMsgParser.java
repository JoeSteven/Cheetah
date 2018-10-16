package com.terminus.iot.msg;

import com.terminus.iot.IoTClient;
import com.terminus.iot.utils.AESUtil;
import com.terminus.iot.utils.ConvertUtil;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */
public class IotMsgParser implements IMsgParser {
    private IoTClient client;

    public IotMsgParser(IoTClient client) {
        this.client = client;
    }

    @Override
    public IotFrame parse(byte[] data) {
        IotFrame ret = client.newFrame();

        byte[] id = new byte[2];
        System.arraycopy(data, 1, id, 0, 2);

        byte[] serviceType = new byte[2];
        System.arraycopy(data, 3, serviceType, 0, 2);

        byte[] cmd = new byte[2];
        System.arraycopy(data, 5, cmd, 0, 2);

        byte[] body = new byte[data.length-1-2-2-2-4];
        System.arraycopy(data, 1+2+2+2, body, 0, body.length);

        byte[] crcB = new byte[4];
        System.arraycopy(data, data.length-4, crcB, 0, 4);

        ret.setMsgType(data[0])
                .setSequenceId(ConvertUtil.bytesToShort(id))
                .setServiceType(ConvertUtil.bytesToShort(serviceType))
                .setCmd(ConvertUtil.bytesToShort(cmd))
                .setBody(AESUtil.decrypt(body, ret.getAesKey()))
                .setCRC32(ConvertUtil.bytesToInt(crcB));
        return ret;
    }
}
