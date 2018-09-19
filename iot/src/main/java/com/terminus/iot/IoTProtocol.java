package com.terminus.iot;

import java.nio.ByteOrder;

/**
 * Description: protocol for IoT
 * author:Joey
 * date:2018/9/18
 */
public class IoTProtocol {
    // Message Type
    public static final  byte MSG_TYPE_SYSTEM = 0;        // 系统消息
    public static final  byte MSG_TYPE_USER = 1;          // 自定义消息

    // Service Type
    public static final short SERVICE_TYPE_COMMON = 0;     // 通用业务
    public static final short SERVICE_TYPE_ACCESS = 1;     // 设备接入业务
    public static final short SERVICE_TYPE_PASS = 2;       // 智慧通行业务
    public static final short SERVICE_TYPE_LOG = 3;        // 日志服务
    public static final short SERVICE_TYPE_SMART_HOME = 4; // 智能家居

    /***
     * 设备接入业务:
     * */
    // 端到云的请求命令
    public static final short CMD_TYPE_UPDATE_AESKEY = 1;      // 更新AES Key。Body的PB模型为TSLIOTUpdateAESKeyRequest
    public static final short CMD_TYPE_WILL = 2;               // 遗愿消息命令。Body为hamcsha1(mac, RSA公钥)。

    // 云到端的请求命令
    public static       short CMD_TYPE_UPDATE_AESKEY_ACK = (short)0x8000;    // 更新AES Key结果。端收到此命令后才能进行后续请求。Body的PB模型为TSLIOTCommonResult
    public static       short CMD_TYPE_UPDATE_SUBSCRIPTION = (short)0x8001;  // 更新端订阅话题列表。Body的PB模型为TSLIOTChangeSubscriptionRequest
    public static       short CMD_TYPE_REFRESH_AESKEY = (short)0x8002;       // 请求设备端重新刷新一次AES KEY，Body为hamcsha1(mac, RSA公钥)。设备端需要重新更新AES Key流程

    /***
     * 通用业务:
     */
    // 端到云的请求命令
    public static final short CMD_TYPE_DEVICE_CONTROL_ACK = 1; // 设备控制结果。Body的PB模型为TSLIOTDeviceControlResult
    public static final short CMD_TYPE_DATA_PASS_THROUGH_ACK = 2; // 数据透传通道响应
    public static final short CMD_TYPE_MODIFY_PWD_ACK = 3; // 修改设备密码结果。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_UPDATE_TIME = 4; // 校时请求。Body为mac（设备唯一标识）utf8 编码
    public static final short CMD_TYPE_UPGRADE_STATUS_REQUEST = 5; // 设备升级状态报告。Body的PB模型为TSLIOTUpgradeStatusRequest
    public static final short CMD_TYPE_UPGRADE_PROGRESS_REQUEST = 6; // 设备升级进度报告。Body的PB模型为 TSLIOTUpgradeProgressRequest
    public static final short CMD_TYPE_ADD_DEVICE_REQUEST = 7; // 添加设备请求。Body的PB模型为 TSLIOTAddDeviceRequest
    public static final short CMD_TYPE_UNBIND_DEVICE_REQUEST = 8; // 网关解绑子设备请求。Body的PB模型为 TSLIOTUnbindDeviceRequest
    public static final short CMD_TYPE_INIT_DATA_ACK = 9; // 设备数据初始化请求结果。Body的PB模型为TSLIOTCommonResult

    // 云到端的请求命令
    public static       short CMD_TYPE_SYSTEM_EXCEPTION = (short)0x8000; // 系统通用处理异常。Body的PB模型为TSLIOTCommonResult
    public static       short CMD_TYPE_DEVICE_CONTROL = (short)0x8001; // 设备控制请求,Body的PB模型为TSLIOTDeviceControlRequest
    public static       short CMD_TYPE_DATA_PASS_THROUGH = (short)0x8002; // 数据透传通道
    public static       short CMD_TYPE_MODIFY_PWD = (short)0x8003; // 修改设备密码，Body的PB模型为TSLIOTModifyPwdRequest
    public static       short CMD_TYPE_UPDATE_TIME_ACK = (short)0x8004; // 校时返回时间戳，Body的PB模型为TSLIOTTimeResult
    public static       short CMD_TYPE_UPGRADE_REQUEST = (short)0x8005; //服务端下发升级通知, Body的PB模型为TSLIOTUpgradeRequest
    public static       short CMD_TYPE_INIT_DATA_REQUEST = (short)0x8006; // 设备数据初始化请求。设备需清除服务器下发的数据，包括住户信息、黑白名单、一次性密码等。Body为TSLIOTCommonRequest。
    public static       short CMD_TYPE_ADD_DEVICE_ACK = (short)0x8007; // 设备数据初始化请求。设备需清除服务器下发的数据，包括住户信息、黑白名单、一次性密码等。Body为TSLIOTCommonRequest。
    /**
     * 智慧通行业务：
     */
// 端到云的请求命令
    public static final short CMD_TYPE_PULL_LIST = 1; // 拉取黑白名单。Body的PB模型为TSLIOTPullListRequest
    public static final short CMD_TYPE_DISPATCH_FILTER_LIST_ACK = 2; // 下发黑白名单结果。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_DISPATCH_FACE_ID_ACK = 3; // 下发人脸识别结果。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_DISPATCH_SIP_ACK = 4; // 下发SIP结果。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_UPDATE_ONCE_PWD_ACK = 5; // 下发一次性密码结果。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_DISPATCH_SIP_CONFIG_ACK = 6; // 下发SIP服务器配置结果。Body的PB模型为TSLIOTCommonResult

    // 云到端的请求命令
    public static       short CMD_TYPE_DISPATCH_FILTER_LIST = (short)0x8002; // 下发黑白名单。Body的PB模型为TSLIOTDispatchFilterListRequest
    public static       short CMD_TYPE_DISPATCH_ONCE_PWD = (short)0x8003; // 下发一次性密码。Body的PB模型为TSLIOTIssueOncePWDRequest
    public static       short CMD_TYPE_DISPATCH_FACE_ID = (short)0x8004; // 下发人脸特征值。Body的PB模型为TSLIOTDispatchFaceListRequest
    public static       short CMD_TYPE_DISPATCH_SIP = (short)0x8005; // 下发SIP呼叫号。Body的PB模型为TSLIOTDispatchSipListRequest
    public static       short CMD_TYPE_DISPATCH_SIP_CONFIG = (short)0x8006; // 下发SIP服务器配置。Body的PB模型为TSLIOTDispatchSipConfigRequest

    /**
     * 日志服务：
     */
// 端到云的请求命令
    public static final short CMD_TYPE_UPDATE_LOG = 1; // 上报设备日志。Body的PB模型为TSLIOTUploadLogRequest
    public static final short CMD_TYPE_SUB_DEVICE_ONLINE = 2; // 子设备在线状态更新。Body的PB模型为TSLIOTOnlineRequest
    public static final short CMD_TYPE_UPDATE_DEVICE_INFO = 3; // 上报设备信息。Body的PB模型为TSLIOTUploadDeviceInfoRequest

    // 云到端的请求命令
    public static       short CMD_TYPE_UPDATE_LOG_ACK = (short)0x8000; // 上报设备日志结果。Body的PB模型为TSLIOTCommonResult

    /**
     * 智能家居：
     */
// 端到云的请求命令
    public static final short CMD_TYPE_UPDATE_ADMIN = 1; // 更新设备管理员信息。Body的PB模型为TSLIOTUpdateAdminRequest

    // 云到端的请求命令
    public static       short CMD_TYPE_UPDATE_ADMIN_ACK = (short)0x8000; // 更新设备管理员信息结果。Body的PB模型为TSLIOTCommonResult

    // 0在线, 1离线, 2防撬报警, 3防劫持报警, 4连续输入密码错误, 5系统锁定, 6锁具欠压上报,7门铃响
    public static class DeviceStatus{
        public static final DeviceStatus ONLINE = new DeviceStatus(0,"online");
        public static final DeviceStatus OFFLINE = new DeviceStatus(1,"offline");
        public static final DeviceStatus PRIZE_ALERT = new DeviceStatus(2,"prize_alert");
        public static final DeviceStatus PASSWORD_ERRORS = new DeviceStatus(3,"password_errors");
        public static final DeviceStatus SYSTEM_LOCK = new DeviceStatus(4,"system_lock");
        public static final DeviceStatus LOCK_UNDERVOLTAGE = new DeviceStatus(5,"lock_undervoltage");
        public static final DeviceStatus DOORBELL_RANG = new DeviceStatus(6,"doorbell_rang");


        private DeviceStatus(int code, String status){
            this.code = code;
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }

        private String status;
        private int code;
    }


    static{
        if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN){
            CMD_TYPE_UPDATE_AESKEY_ACK      = 0x0080;
            CMD_TYPE_UPDATE_SUBSCRIPTION    = 0x0180;
            CMD_TYPE_REFRESH_AESKEY         = 0x0280;

            CMD_TYPE_SYSTEM_EXCEPTION       = 0x0080;
            CMD_TYPE_DEVICE_CONTROL         = 0x0180;
            CMD_TYPE_DATA_PASS_THROUGH      = 0x0280;
            CMD_TYPE_MODIFY_PWD             = 0x0380;
            CMD_TYPE_UPDATE_TIME_ACK        = 0x0480;
            CMD_TYPE_UPGRADE_REQUEST        = 0x0580;
            CMD_TYPE_INIT_DATA_REQUEST      = 0x0680;
            CMD_TYPE_ADD_DEVICE_ACK         = 0x0780;

            CMD_TYPE_DISPATCH_FILTER_LIST   = 0x0280;
            CMD_TYPE_DISPATCH_ONCE_PWD      = 0x0380;
            CMD_TYPE_DISPATCH_FACE_ID       = 0x0480;
            CMD_TYPE_DISPATCH_SIP           = 0x0580;
            CMD_TYPE_DISPATCH_SIP_CONFIG    = 0x0680;


            CMD_TYPE_UPDATE_LOG_ACK         = 0x0080;
            CMD_TYPE_UPDATE_ADMIN_ACK       = 0x0080;
        }
    }
}
