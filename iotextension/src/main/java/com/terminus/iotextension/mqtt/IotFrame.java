package com.terminus.iotextension.mqtt;

import com.terminus.iot.IoTProtocol;
import com.terminus.iot.utils.AESUtil;
import com.terminus.iot.utils.ConvertUtil;
import com.terminus.iot.utils.Crc32Utils;
import com.terminus.iot.utils.RSAUtil;

import java.util.Arrays;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */
public class IotFrame {
    private byte MsgType;     //1byte
    private short SequenceId;  //2byte
    private short ServiceType; //2byte 0:通用业务，1:接入业务，2:智慧通行业务
    private short Cmd;         //2Byte,
    private byte[] Body;        //N byte
    private int CRC32;       //4byte;

    private String rsaKey;
    private byte[] aesKey;

    public IotFrame() {

    }

    public IotFrame(byte msgType, short sequenceId, short serviceType, short cmd, byte[] body) {
        MsgType = msgType;
        SequenceId = sequenceId;
        ServiceType = serviceType;
        Cmd = cmd;
        Body = body;
    }

    public IotFrame setAesKey(byte[] aesKey) {
        this.aesKey = aesKey;
        return this;
    }

    public IotFrame setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
        return this;
    }

    public byte getMsgType() {
        return MsgType;
    }

    public IotFrame setMsgType(byte msgType) {
        MsgType = msgType;
        return this;
    }

    public short getSequenceId() {
        return SequenceId;
    }

    public IotFrame setSequenceId(short sequenceId) {
        SequenceId = sequenceId;
        return this;
    }

    public short getServiceType() {
        return ServiceType;
    }

    public IotFrame setServiceType(short serviceType) {
        ServiceType = serviceType;
        return this;
    }

    public short getCmd() {
        return Cmd;
    }

    public IotFrame setCmd(short cmd) {
        Cmd = cmd;
        return this;
    }

    public byte[] getBody() {
        return Body;
    }

    public IotFrame setBody(byte[] body) {
        Body = body;
        return this;
    }

    public int getCRC32() {
        return CRC32;
    }

    public IotFrame setCRC32(int CRC32) {
        this.CRC32 = CRC32;
        return this;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public byte[] getAesKey() {
        return aesKey;
    }

    public byte[] toByte() {
        byte[] ret = toByteWithoutCrc();
        long crcl = Crc32Utils.CRC32(ret);
        byte[] crc = ConvertUtil.intToBytes((int) crcl);
        ret = ConvertUtil.byteMerger(ret, crc);
        return ret;
    }

    //will 帧   Body = hamcsha1(mac, RSA公钥)
    public byte[] toByteWithoutCrc() {
        byte[] ret = {MsgType};
        ret = ConvertUtil.byteMerger(ret, ConvertUtil.shortToBytes(SequenceId));
        ret = ConvertUtil.byteMerger(ret, ConvertUtil.shortToBytes(ServiceType));
        ret = ConvertUtil.byteMerger(ret, ConvertUtil.shortToBytes(Cmd));

        if (MsgType == IoTProtocol.MSG_TYPE_SYSTEM && ServiceType == IoTProtocol.SERVICE_TYPE_DEVICE && Cmd == IoTProtocol.CMD_TYPE_DEV_UPDATE_AESKEY) {
            Body = RSAUtil.EncodeDefault(rsaKey, Body);
        } else if (MsgType == IoTProtocol.MSG_TYPE_SYSTEM && ServiceType == IoTProtocol.SERVICE_TYPE_DEVICE && Cmd == IoTProtocol.CMD_TYPE_DEV_WILL) {

        } else if (MsgType == IoTProtocol.MSG_TYPE_SYSTEM && ServiceType == IoTProtocol.SERVICE_TYPE_COMMON && Cmd == IoTProtocol.CMD_TYPE_SERVER_WILL) {

        } else {
            Body = AESUtil.encrypt(Body, aesKey);
        }

        ret = ConvertUtil.byteMerger(ret, Body);
        return ret;
    }

    @Override
    public String toString() {
        return "IotFrame{" +
                "MsgType=" + MsgType +
                ", SequenceId=" + SequenceId +
                ", ServiceType=" + ServiceType +
                ", Cmd=" + Cmd +
                ", Body=" + Arrays.toString(Body) +
                '}';
    }
}
