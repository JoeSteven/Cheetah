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
import com.terminus.iotextension.event.PersonInfoEvent;
import com.terminus.iotextension.iot.config.DataType;
import com.terminus.iotextension.iot.config.DevStatus;
import com.terminus.iotextension.iot.config.Direction;
import com.terminus.iotextension.iot.config.OpenStatus;
import com.terminus.iotextension.iot.config.OpenType;
import com.terminus.iotextension.iot.config.PersonType;
import com.terminus.iotextension.mqtt.IotFrame;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTCommon;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDataSync;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDevice;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
                        if (!isConnect) {
                            mIoTClient.subscribe(new String[]{IoTConstant.SUB_TOPIC},new int[1]);

                            updateAesKey();
                            isConnect = true;
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
    }

    //数据业务
    private void dataMsg(IotFrame frame) {
        switch (frame.getCmd()) {
            case IoTProtocol.CMD_TYPE_DISPATCH_PERSON:
                workPersonList(frame);
                break;
            default:
                break;
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

                EventBus.getDefault().post(infoEvent);
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

    //TODO:日志业务 云端的回复,后续根据需求进行完善
    private void logMsg(IotFrame frame) {

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
                String check = Sha1Util.EncodeDefault(IoTConstant.RSA_KEY,IoTConstant.MAC.getBytes());
                assert check != null;
                if (check.equals(new String(frame.getBody()))) {
                    IoTConstant.AES_KEY = AESUtil.getNewAesKey();
                    updateAesKey();
                }
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
                              String cardNo, String imgUrl, String videoUrl) {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_BUSINESS_LOG,
                IoTProtocol.CMD_TYPE_UPLOAD_PASS_LOG,
                IotPBUtil.constructPassLog(personId, feature, personType, direction,
                        time, openStatus, devStatus, openType, cardNo, imgUrl, videoUrl));

        sendFrame(frame);
    }

    /**
     * 上传所需数据类型
     */
     @Override
     public void uploadNeedInfo() {
        IotFrame frame = newFrame(
                IoTProtocol.MSG_TYPE_SYSTEM,
                mIoTClient.generateId(),
                IoTProtocol.SERVICE_TYPE_DATA_SYNC,
                IoTProtocol.CMD_TYPE_UPLOAD_REQUIREMENT,
                IotPBUtil.constructNeedDataSync(IoTConstant.NEEDITEMS));

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
                    break;
                case "open":
                    break;
                case "clear":
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
                mIotMessageCallback.onSuccess();
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

        EventBus.getDefault().post(new AeskeyEvent(IoTConstant.AES_KEY));

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
