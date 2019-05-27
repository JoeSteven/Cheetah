package com.terminus.iotextension.iot;

import com.terminus.iot.IoTClient;
import com.terminus.iotextension.iot.config.DataType;
import com.terminus.iotextension.iot.config.DevStatus;
import com.terminus.iotextension.iot.config.Direction;
import com.terminus.iotextension.iot.config.OpenStatus;
import com.terminus.iotextension.iot.config.OpenType;
import com.terminus.iotextension.iot.config.PersonType;

import java.util.List;

/**
 * @author rain
 * @date 2019/4/28
 */
public class IotController extends MqttImpl {

    private MqttImpl mIot;

    private volatile static IotController INSTANCE = null;

    private IotController() {
        mIot = new IotRespository();
    }

    private static IotController create() {
        if (INSTANCE == null) {
            INSTANCE = new IotController();
        }

        return INSTANCE;
    }

    @Override
    public IoTClient initClient() {
        return mIot.initClient();
    }

    @Override
    public void connect() {
        mIot.connect();
    }

    @Override
    public void disConnect() {
        mIot.disConnect();
    }

    @Override
    public void reConnect() {
        mIot.reConnect();
    }

    @Override
    public void uploadNeedInfo() {
        mIot.uploadNeedInfo();
    }

    @Override
    public void requestQr() {
        mIot.requestQr();
    }

    @Override
    public void requestRule() {
        mIot.requestRule();
    }

    @Override
    public void requestSetting() {
        mIot.requestSetting();
    }

    @Override
    public void requestTime() {
        mIot.requestTime();
    }

    @Override
    public void errorInfo(int personId, PersonType type, long version, String customInfo) {
        mIot.errorInfo(personId, type, version, customInfo);
    }

    @Override
    public void asylocalData(DataType type, long version) {
        mIot.asylocalData(type, version);
    }

    @Override
    public void uploadPassLog(int personId, String feature, PersonType personType, Direction direction,
                              long time, OpenStatus openStatus, DevStatus devStatus, OpenType openType,
                              String cardNo, String imgUrl, String videoUrl) {
        mIot.uploadPassLog(personId, feature, personType, direction,
                time, openStatus, devStatus, openType, cardNo, imgUrl, videoUrl);
    }

    public void setMessageCallback(IotMessageCallback messageCallback) {
        mIot.mIotMessageCallback = messageCallback;
    }

    public static class Bulider {

        public Bulider address(String address) {
            IoTConstant.TCP_ADDRESS = address;
            return this;
        }

        public Bulider devID(String devId) {
            IoTConstant.DEV_ID = devId;
            return this;
        }

        public Bulider clientID(String clientId) {
            IoTConstant.CLIENT_ID = clientId;
            return this;
        }

        public Bulider userName(String userName) {
            IoTConstant.USER_NAME = userName;
            return this;
        }

        public Bulider passWord(String password) {
            IoTConstant.PASSWORD = password;
            return this;
        }

        public Bulider subTopic(String upTopic) {
            IoTConstant.SUB_TOPIC = upTopic;
            return this;
        }

        public Bulider pubTopic(String downTopic) {
            IoTConstant.PUB_TOPIC = downTopic;
            return this;
        }

        public Bulider type(String type) {
            IoTConstant.TYPE = type;
            return this;
        }

        public Bulider devName(String devName) {
            IoTConstant.DEVNAME = devName;
            return this;
        }

        public Bulider model(String model) {
            IoTConstant.MODEL = model;
            return this;
        }

        public Bulider category(String category) {
            IoTConstant.CATEGORY = category;
            return this;
        }

        public Bulider sn(String sn) {
            IoTConstant.SN = sn;
            return this;
        }

        public Bulider mac(String mac) {
            IoTConstant.MAC = mac;
            return this;
        }

        public Bulider softVersion(String softVersion) {
            IoTConstant.SOFTWARE_VERSION = softVersion;
            return this;
        }

        public Bulider hardVersion(String harVersion) {
            IoTConstant.HARDWARE_VERSION = harVersion;
            return this;
        }

        public Bulider functions(List<Integer> functions) {
            IoTConstant.FUNCTIONS = functions;
            return this;
        }

        public Bulider needItems(List<DataType> items) {
            IoTConstant.NEEDITEMS = items;
            return this;
        }

        public Bulider aesKey(byte[] aesKey) {
            IoTConstant.AES_KEY = aesKey;
            return this;
        }

        public IotController build() {
            return create();
        }
    }
}
