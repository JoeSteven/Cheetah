package com.terminus.iotextension.iot;

import com.terminus.iot.IoTClient;
import com.terminus.iotextension.iot.config.DataType;
import com.terminus.iotextension.iot.config.DevStatus;
import com.terminus.iotextension.iot.config.Direction;
import com.terminus.iotextension.iot.config.NetType;
import com.terminus.iotextension.iot.config.OpenStatus;
import com.terminus.iotextension.iot.config.OpenType;
import com.terminus.iotextension.iot.config.PersonError;
import com.terminus.iotextension.iot.config.PersonType;
import com.terminus.iotextension.iot.config.UpdateType;

import java.util.List;

/**
 * @author rain
 * @date 2019/4/28
 */
public class IotController extends Mqtt {

    private Mqtt mIot;

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

    private static IotController create(Mqtt mqtt) {
        if (INSTANCE == null) {
            INSTANCE = new IotController(mqtt);
        }

        return INSTANCE;
    }

    public IotController(Mqtt mqtt) {
        this.mIot = mqtt;
    }

    @Override
    public IoTClient initClient() {
        return mIot.initClient();
    }

    /**
     * Mqtt连接
     */
    @Override
    public void connect() {
        mIot.connect();
    }

    /**
     * Mqtt断开连接
     */
    @Override
    public void disConnect() {
        mIot.disConnect();
    }

    /**
     * 强制断链
     */
    @Override
    public void disConnectForce() {
        mIot.disConnectForce();
    }

    /**
     * Mqtt重链
     */
    @Override
    public void reConnect() {
        mIot.reConnect();
    }

    /**
     * 上传本地设备的网络配置信息
     * @param netType 网络类型
     * @param netName 网卡名称
     * @param outIp 外网ip地址
     * @param innerIp 局域网ip地址
     * @param mask 掩码
     * @param gateWay 网关
     * @param dns1 DNS1
     * @param dns2 DNS2
     */
    @Override
    public void uploadNetInfo(NetType netType, String netName, String outIp, String innerIp, String mask
            , String gateWay, String dns1, String dns2) {
        mIot.uploadNetInfo(netType,netName,outIp,innerIp,mask,gateWay,dns1,dns2);
    }

    /**
     * 上报本地客户端所需的数据
     */
    @Override
    public void uploadNeedInfo(DataType... dataType) {
        mIot.uploadNeedInfo(dataType);
    }

    /**
     * 关闭Mqtt
     */
    @Override
    public void close() {
        mIot.close();
    }

    /**
     * 请求二维码扫描规则
     */
    @Override
    public void requestQr() {
        mIot.requestQr();
    }

    /**
     * 请求设备的通行规
     */
    @Override
    public void requestRule() {
        mIot.requestRule();
    }

    /**
     * 请求配置信息
     */
    @Override
    public void requestSetting() {
        mIot.requestSetting();
    }

    /**
     * 时间同步请求
     */
    @Override
    public void requestTime() {
        mIot.requestTime();
    }

    /**
     * 上报设备端使用人员数据时的错误信息
     * @param personId 用户ID
     * @param type 人员类型
     * @param error 错误类型
     * @param version 人员版本
     * @param customInfo 错误描述
     */
    @Override
    public void errorInfo(int personId, PersonType type, PersonError error,long version, String customInfo) {
        mIot.errorInfo(personId, type, error,version, customInfo);
    }

    /**
     * 同步数据
     * @param type 数据类型
     * @param version 版本号
     */
    @Override
    public void asylocalData(DataType type, long version) {
        mIot.asylocalData(type, version);
    }

    /**
     * 设备升级状态报告
     */
    @Override
    public void updateAck(UpdateType type, long time, long taskId, UpdateType.ErrorCode errorCode, String message) {
        mIot.updateAck(type, time, taskId, errorCode, message);
    }

    /**
     * 上传通行日志信息
     * @param personId 用户ID
     * @param feature 人员特征信息(json)
     * @param personType 人员类型
     * @param direction 进出方向
     * @param time 通行时间
     * @param openStatus 开门状态
     * @param devStatus 设备状态
     * @param openType 开门方式
     * @param cardNo 卡号
     * @param imgUrl 图片地址
     * @param videoUrl 视频地址
     * @param reserve 自定义信息
     * @param logId 设备端日志ID
     */
    @Override
    public void uploadPassLog(int personId, String feature, PersonType personType, Direction direction,
                              long time, OpenStatus openStatus, DevStatus devStatus, OpenType openType,
                              String cardNo, String imgUrl, String videoUrl,String reserve,long logId) {
        mIot.uploadPassLog(personId, feature, personType, direction,
                time, openStatus, devStatus, openType, cardNo, imgUrl, videoUrl,reserve,logId);
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

        public Bulider aesKey(byte[] aesKey) {
            IoTConstant.AES_KEY = aesKey;
            return this;
        }

        public IotController build() {
            return create();
        }
    }
}
