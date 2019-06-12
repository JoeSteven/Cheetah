package com.terminus.iotextension.iot;

import android.text.TextUtils;
import android.util.Log;

import com.terminus.iot.BuildConfig;
import com.terminus.iot.IoTClient;
import com.terminus.iot.IoTProtocol;
import com.terminus.iot.utils.AESUtil;
import com.terminus.iot.utils.ConvertUtil;
import com.terminus.iot.utils.Sha1Util;
import com.terminus.iotextension.event.AeskeyEvent;
import com.terminus.iotextension.event.CodeEvent;
import com.terminus.iotextension.event.DoorEvent;
import com.terminus.iotextension.event.PasslogResultEvent;
import com.terminus.iotextension.event.PersonInfoEvent;
import com.terminus.iotextension.event.ResetEvent;
import com.terminus.iotextension.event.RoomInfoEvent;
import com.terminus.iotextension.event.RuleEvent;
import com.terminus.iotextension.event.ServerEvent;
import com.terminus.iotextension.event.TimeEvent;
import com.terminus.iotextension.event.UninstallEvent;
import com.terminus.iotextension.iot.config.DataType;
import com.terminus.iotextension.iot.config.DevStatus;
import com.terminus.iotextension.iot.config.Direction;
import com.terminus.iotextension.iot.config.NetType;
import com.terminus.iotextension.iot.config.OpenStatus;
import com.terminus.iotextension.iot.config.OpenType;
import com.terminus.iotextension.iot.config.PersonType;
import com.terminus.iotextension.mqtt.IotFrame;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTBusinessLog;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTCommon;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDataSync;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDevice;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author rain
 * @date 2019/4/28
 */
public class IotRespository extends MqttImpl {

    private boolean isConnect;

    @Override
    public IoTClient initClient() {
        mIoTClient = IoTClient.with(IoTConstant.TCP_ADDRESS)
                .devId(IoTConstant.DEV_ID)
                .clientId(IoTConstant.CLIENT_ID)
                .userName(IoTConstant.USER_NAME)
                .password(IoTConstant.PASSWORD.toCharArray())
                .rsaKey(IoTConstant.RSA_KEY)
                .autoReconnect(true)
                .cleanSession(true)
                .willTopic(IoTConstant.PUB_TOPIC)
                .willBytes(newFrame(IoTProtocol.MSG_TYPE_SYSTEM,
                        Short.MAX_VALUE,
                        IoTProtocol.SERVICE_TYPE_DEVICE,
                        IoTProtocol.CMD_TYPE_DEV_WILL,
                        Sha1Util.EncodeDefaultForBytes(IoTConstant.RSA_KEY,IoTConstant.DEV_ID.getBytes())).toByte())
                .create();

        return mIoTClient;
    }

    /**
     * 连接成功后即开始进行订阅
     */
    @Override
    public void connect() {
        if (!check()) {
            return;
        }

        try {
            mIoTClient.connect(new IotExtendedCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    if (message == null || message.getPayload() == null || message.getPayload().length < 0)
                        return;
                    byte[] data = message.getPayload();
                    IotFrame ret = newFrame();

                    byte[] id = new byte[2];
                    System.arraycopy(data, 1, id, 0, 2);

                    byte[] serviceType = new byte[2];
                    System.arraycopy(data, 3, serviceType, 0, 2);

                    byte[] cmd = new byte[2];
                    System.arraycopy(data, 5, cmd, 0, 2);

                    byte[] body = new byte[data.length - 1 - 2 - 2 - 2 - 4];
                    System.arraycopy(data, 1 + 2 + 2 + 2, body, 0, body.length);

                    byte[] crcB = new byte[4];
                    System.arraycopy(data, data.length - 4, crcB, 0, 4);

                    ret.setMsgType(data[0])
                            .setSequenceId(ConvertUtil.bytesToShort(id))
                            .setServiceType(ConvertUtil.bytesToShort(serviceType))
                            .setCmd(ConvertUtil.bytesToShort(cmd))
                            .setBody(AESUtil.decrypt(body, ret.getAesKey()))
                            .setCRC32(ConvertUtil.bytesToInt(crcB));

                    messageWorked(ret);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    try {
                        if (!isConnect && !reconnect) {
                            mIoTClient.subscribe(new String[]{IoTConstant.SUB_TOPIC},new int[1]);

                            updateAesKey();
                            isConnect = true;
                        }

                        if (mIotMessageCallback != null) {
                            mIotMessageCallback.onSuccess(reconnect);
                        }

                    } catch (MqttException e) {
                        if (mIotMessageCallback != null) {
                            mIotMessageCallback.onError(e);
                        }
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    if (mIotMessageCallback != null) {
                        mIotMessageCallback.onError(cause);
                    }
                }
            });

        } catch (MqttException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onError(e);
            }
        }
    }

    @Override
    public void disConnect() {
        if (!check()) {
            return;
        }

        isConnect = false;

        if (mIoTClient.isConnected()) {
            try {
                mIoTClient.disconnect();
            } catch (MqttException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, e.getMessage());
                }

                if (mIotMessageCallback != null) {
                    mIotMessageCallback.onError(e);
                }
            }
        }
    }

    @Override
    public void reConnect() {
        if (!check()) {
            return;
        }

        if (!mIoTClient.isConnected()) {
            try {
                mIoTClient.reconnect();
            } catch (MqttException e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, e.getMessage());
                }

                if (mIotMessageCallback != null) {
                    mIotMessageCallback.onError(e);
                }
            }
        }
    }

    @Override
    public void uploadNetInfo(NetType netType, String netName, String outIp, String innerIp,
                              String mask, String gateWay, String dns1, String dns2) {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DEVICE,
                IoTProtocol.CMD_TYPE_DEV_INFO_NET,
                IotPBUtil.constructNetInfo(netType,netName,outIp,innerIp,mask,gateWay,dns1,dns2));

        sendFrame(frame);
    }

    private void messageWorked(IotFrame frame) {
        switch (frame.getMsgType()) {
            case IoTProtocol.MSG_TYPE_SYSTEM:
                processSystemMsg(frame);
            case IoTProtocol.MSG_TYPE_USER:
                processUserMsg(frame);
            default:
                break;
        }
    }

    private void processSystemMsg(IotFrame frame) {
        switch (frame.getServiceType()) {
            case IoTProtocol.SERVICE_TYPE_DEVICE:
                accessMsg(frame);
                break;
            case IoTProtocol.SERVICE_TYPE_COMMON:
                commonMsg(frame);
                break;
            case IoTProtocol.SERVICE_TYPE_DATA_SYNC:
                dataMsg(frame);
                break;
            case IoTProtocol.SERVICE_TYPE_BUSINESS_LOG:
                logMsg(frame);
                break;
            default:
                break;
        }
    }

    //通用业务
    private void commonMsg(IotFrame frame) {
        switch (frame.getCmd()) {
            case IoTProtocol.CMD_TYPE_QR_INFO:
                codeInfo(frame);
                break;
            case IoTProtocol.CMD_TYPE_PASS_RULE:
                passRule(frame);
                break;
            case IoTProtocol.CMD_TYPE_PLATFORM_SETTING:
                serverConfig(frame);
                break;
            case IoTProtocol.CMD_TYPE_UPDATE_TIME_ACK:
                checkTime(frame);
                break;
            default:
                break;
        }
    }

    /**
     * 校时
     */
    private void checkTime(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());

        try {
            TSLIOTCommon.TSLIOTTimeResult result =
                    TSLIOTCommon.TSLIOTTimeResult.parseFrom(input);

            long time = result.getTime();

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onEvent(new TimeEvent(time));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统设置
     */
    private void serverConfig(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());

        try {
            TSLIOTCommon.TSLIOTPlatformSetting result =
                    TSLIOTCommon.TSLIOTPlatformSetting.parseFrom(input);

            boolean network = result.getAccessNetwork();
            String zimgUrl = result.getZimgUrlPrefix();
            String httpUrl = result.getHttpUrlPrefix();

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onEvent(new ServerEvent(network,zimgUrl,httpUrl));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备规则校验字段
     */
    private void passRule(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());

        try {
            TSLIOTCommon.TSLIOTPassRule result =
                    TSLIOTCommon.TSLIOTPassRule.parseFrom(input);

            String projectId = result.getPassPermissionId();
            String personId = result.getPassPersonId();

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onEvent(new RuleEvent(personId,projectId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 二维码解密规则
     */
    private void codeInfo(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());

        try {
            TSLIOTCommon.TSLIOTQrCodeInfo result =
                    TSLIOTCommon.TSLIOTQrCodeInfo.parseFrom(input);

            String aesKey = result.getAesKey();
            String rsaKey = result.getRsaPublicKey();
            int duration = result.getEffectiveDuration();

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onEvent(new CodeEvent(aesKey,rsaKey,duration));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //数据业务
    private void dataMsg(IotFrame frame) {
        switch (frame.getCmd()) {
            case IoTProtocol.CMD_TYPE_DISPATCH_PERSON:
                workPersonList(frame);
                break;
            case IoTProtocol.CMD_TYPE_DISPATCH_ROOM_INFO:
                workRoomList(frame);
                break;
            default:
                break;
        }
    }

    /**
     * 房间信息请求平台端的回复
     * @param frame PB
     */
    private void workRoomList(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());
        try {
            TSLIOTDataSync.TSLIOTDispatchRoomInfoRequest result =
                    TSLIOTDataSync.TSLIOTDispatchRoomInfoRequest.parseFrom(input);

            if (result.getListList().size() > 0) {
                RoomInfoEvent infoEvent = new RoomInfoEvent(result.getDevId(),result.getVersion(),
                        result.getMore(),result.getListList());

                if (mIotMessageCallback != null) {
                    mIotMessageCallback.onEvent(infoEvent);
                }
            }

        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onError(e);
            }
        }
    }

    /**
     * 同步数据之后平台端的回复
     * @param frame PB
     */
    private void workPersonList(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());
        try {
            TSLIOTDataSync.TSLIOTDispatchPersonListRequest result =
                    TSLIOTDataSync.TSLIOTDispatchPersonListRequest.parseFrom(input);

            if (result.getListList().size() > 0) {
                PersonInfoEvent infoEvent = new PersonInfoEvent(result.getDevId(),result.getVersion(),
                        result.getMore(),result.getListList());

                if (mIotMessageCallback != null) {
                    mIotMessageCallback.onEvent(infoEvent);
                }
            }

        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onError(e);
            }
        }
    }

    private void logMsg(IotFrame frame) {
        switch (frame.getCmd()) {
            case IoTProtocol.CMD_TYPE_UPLOAD_PASS_LOG_ACK:
                passLogResult(frame);
                break;
            default:
                break;
        }
    }

    /**
     * 数据上传结果
     * @param frame
     */
    private void passLogResult(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());
        try {
            TSLIOTBusinessLog.TSLIOTUploadPassLogResult result =
                     TSLIOTBusinessLog.TSLIOTUploadPassLogResult.parseFrom(input);

            if (result.getLogResultsList().size() > 0) {
                PasslogResultEvent event = new PasslogResultEvent(result.getLogResultsList(),
                        result.getLogResultsCount());

                if (mIotMessageCallback != null) {
                    mIotMessageCallback.onEvent(event);
                }
            }

        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onError(e);
            }
        }
    }

    /**
     * 设备接入业务
     * @param frame 数据结构体
     */
    private void accessMsg(IotFrame frame) {
        switch (frame.getCmd()) {
            case IoTProtocol.CMD_TYPE_UPDATE_AESKEY_ACK:
                checkAesResult(frame);
                break;
            case IoTProtocol.CMD_TYPE_REFRESH_AESKEY:
                IoTConstant.AES_KEY = AESUtil.getNewAesKey();
                updateAesKey();
                break;
            case IoTProtocol.CMD_TYPE_CONTROL_CMD:
                controlDevice(frame);
                break;
            default:
                break;
        }

    }

    /**
     * 同步本地数据
     */
    @Override
    public void asylocalData(DataType type, long version) {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DATA_SYNC,
                IoTProtocol.CMD_TYPE_PULL_DATA,
                IotPBUtil.constructPullDataSync(type,version));

        sendFrame(frame);
    }

    @Override
    public void uploadPassLog(int personId, String feature, PersonType personType, Direction direction,
                              long time, OpenStatus openStatus, DevStatus devStatus, OpenType openType,
                              String cardNo, String imgUrl, String videoUrl,String reserve,long logId) {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_BUSINESS_LOG,
                IoTProtocol.CMD_TYPE_UPLOAD_PASS_LOG,
                IotPBUtil.constructPassLog(personId, feature, personType, direction,
                        time, openStatus, devStatus, openType, cardNo, imgUrl, videoUrl,
                        reserve,logId));

        sendFrame(frame);
    }

    /**
     * 上传所需数据
     */
     @Override
     public void uploadNeedInfo(DataType... type) {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DATA_SYNC,
                IoTProtocol.CMD_TYPE_UPLOAD_REQUIREMENT,
                IotPBUtil.constructNeedDataSync(Arrays.asList(type)));

         sendFrame(frame);
     }

    @Override
    public void close() {
        if (!check()) {
            return;
        }

        try {
            mIoTClient.close();
        } catch (MqttException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onError(e);
            }
        }
    }

    @Override
    public void requestQr() {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_COMMON,
                IoTProtocol.CMD_TYPE_GET_QR_INFO,
                IotPBUtil.constructCommonRequest());

        sendFrame(frame);
    }

    @Override
    public void requestRule() {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_COMMON,
                IoTProtocol.CMD_TYPE_GET_PASS_RULE,
                IotPBUtil.constructCommonRequest());

        sendFrame(frame);
    }

    @Override
    public void requestSetting() {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_COMMON,
                IoTProtocol.CMD_TYPE_GET_PLATFORM_SETTING,
                IotPBUtil.constructCommonRequest());

        sendFrame(frame);
    }

    @Override
    public void requestTime() {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_COMMON,
                IoTProtocol.CMD_TYPE_UPDATE_TIME,
                IotPBUtil.constructCommonRequest());

        sendFrame(frame);
    }

    @Override
    public void errorInfo(int personId,PersonType type,long version,String customInfo) {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DATA_SYNC,
                IoTProtocol.CMD_TYPE_PERSON_INFO_ERROR,
                IotPBUtil.constructPersonError(personId,type,version,customInfo));

        sendFrame(frame);
    }

    /**
     * TODO:后续根据需求进行完善
     * 设备控制
     * @param frame 数据帧
     */
    private void controlDevice(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());
        try {
            TSLIOTDevice.TSLIOTDeviceControlCmdRequest cmdResult =
                    TSLIOTDevice.TSLIOTDeviceControlCmdRequest.parseFrom(input);
            String cmd = cmdResult.getCmd();
            switch (cmd) {
                case "uninstall":
                    if (mIotMessageCallback != null) {
                        mIotMessageCallback.onEvent(new UninstallEvent(true));
                    }
                    break;
                case "open":
                    if (mIotMessageCallback != null) {
                        mIotMessageCallback.onEvent(new DoorEvent(true));
                    }
                    break;
                case "clear":
                    if (mIotMessageCallback != null) {
                        mIotMessageCallback.onEvent(new ResetEvent(true));
                    }
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查AESKEY
     * @param frame 数据帧
     */
    private void checkAesResult(IotFrame frame) {
        InputStream input = new ByteArrayInputStream(frame.getBody());
        try {
            TSLIOTCommon.TSLIOTCommonResult comRet =
                    TSLIOTCommon.TSLIOTCommonResult.parseFrom(input);
            if (comRet.getCode() != 0) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "update aes key failed:" + comRet + "," + "id:" + frame.getSequenceId());
                }
            } else {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "update aes key success" + "," + "id:" + frame.getSequenceId());
                }

                registerDevice();
                //注册成功
                mIotMessageCallback.onSuccess(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册设备
     */
    private void registerDevice() {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DEVICE,
                IoTProtocol.CMD_TYPE_DEV_REGISTER,
                IotPBUtil.constructDeviceInfo());

        sendFrame(frame);
    }

    /**
     * 更新AES Key
     */
    private void updateAesKey() {
        if (TextUtils.isEmpty(new String(IoTConstant.AES_KEY))) {
            IoTConstant.AES_KEY = AESUtil.getNewAesKey();
        }

        if (mIotMessageCallback != null) {
            mIotMessageCallback.onEvent(new AeskeyEvent(IoTConstant.AES_KEY));
        }

        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DEVICE,
                IoTProtocol.CMD_TYPE_DEV_UPDATE_AESKEY,
                IotPBUtil.generateUpdateAESKey(IoTConstant.AES_KEY));

        sendFrame(frame);
    }

    private void sendFrame(IotFrame frame) {
        try {
            send(IoTConstant.PUB_TOPIC, frame.getSequenceId(), frame.toByte());
        } catch (MqttException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, e.getMessage());
            }

            if (mIotMessageCallback != null) {
                mIotMessageCallback.onError(e);
            }
        }
    }

    //TODO:用户自定义消息,后续根据需求完善
    private void processUserMsg(IotFrame frame) {
    }

    private IotFrame newFrame(byte msgType, short sequenceId, short serviceType, short cmd,
                              byte[] body) {
        return new IotFrame(msgType, sequenceId, serviceType, cmd, body)
                .setRsaKey(IoTConstant.RSA_KEY)
                .setAesKey(IoTConstant.AES_KEY);
    }

    private IotFrame newFrame() {
        return new IotFrame()
                .setRsaKey(IoTConstant.RSA_KEY)
                .setAesKey(IoTConstant.AES_KEY);
    }
}
