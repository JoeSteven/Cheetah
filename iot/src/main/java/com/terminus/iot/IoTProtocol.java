package com.terminus.iot;

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
    public static final short SERVICE_TYPE_COMMON = 0;       // 通用业务
    public static final short SERVICE_TYPE_DEVICE = 1;       // 设备接入业务
    public static final short SERVICE_TYPE_DATA_SYNC = 2;    // 数据同步
    public static final short SERVICE_TYPE_BUSINESS_LOG = 3; // 日志服务

    /***
     * 设备接入业务:
     * */
    public static final short CMD_TYPE_DEV_UPDATE_AESKEY = 1;             // 更新AES Key。 Body的PB模型为 TSLIOTUpdateAESKeyRequest
    public static final short CMD_TYPE_DEV_WILL = 2;                      // 遗愿消息命令。Body为 hamcsha1(dev_id, RSA公钥)。
    public static final short CMD_TYPE_DEV_REGISTER = 3;                  // 设备注册消息。Body的PB模型为 TSLIOTRegisterDeviceRequest
    public static final short CMD_TYPE_DEV_STATUS = 4;                    // 设备状态上报。Body的PB模型为 TSLIOTDeviceStatusRequest
    public static final short CMD_TYPE_DEV_OPERATE_LOG = 5;               // 设备操作日志上报。Body的PB模型为 TSLIOTDeviceLogOperateRequest
    public static final short CMD_TYPE_DEV_WARNING = 6;                   // 设备事件上报。Body的PB模型为 TSLIOTDeviceWarningRequest
    public static final short CMD_TYPE_DEV_CONTROL_CMD_ACK = 7;           // 设备端执行远程命令的结果。Body的PB模型为 TSLIOTDeviceControlCmdResponse message部分为执行结果，如果不支持返回“undefine”
    public static final short CMD_TYPE_DEV_INFO_NET = 8;                  // 设备网络配置  Body的PB模型为 TSLIOTDeviceNetInfo
    public static final short CMD_TYPE_DEV_INFO_SYSTEM = 9;               // 设备系统信息  Body的PB模型为 TSLIOTDeviceSystemInfo
    public static final short CMD_TYPE_DEV_PWD_INFO = 10;                 // 设备密码      Body的PB模型为 TSLIOTPwdInfoRequest

    // 云到端的请求命令
    public static final short CMD_TYPE_UPDATE_AESKEY_ACK = (short)0x8001;        // 更新AES Key结果。端收到此命令后才能进行后续请求。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_CONTROL_CMD = (short)0x8007;              // 服务端请求设备执行命令 Body为 TSLIOTDeviceControlCmdRequest
    public static final short CMD_TYPE_UPDATE_SUBSCRIPTION = (short)0x8008;      // 更新端订阅话题列表。Body的PB模型为TSLIOTChangeSubscriptionRequest
    public static final short CMD_TYPE_REFRESH_AESKEY = (short)0x8009;           // 请求设备端重新刷新一次AES KEY，Body为hamcsha1(mac, RSA公钥)。设备端需要重新更新AES Key流程
    public static final short CMD_TYPE_DEV_MOD_PWD = (short)0x8010;              // 设备密码修改      Body的PB模型为 TSLIOTPwdInfoRequest

    /***
     * 数据同步业务:
     */
    // 端到云的请求命令
    public static final short CMD_TYPE_PULL_DATA = 1;                         // 拉取数据。Body的PB模型为TSLIOTPullDataRequest
    public static final short CMD_TYPE_UPDATE_ONCE_PWD_ACK = 5;       	      // 下发一次性密码结果。Body的PB模型为TSLIOTCommonResult
    public static final short CMD_TYPE_UPLOAD_REQUIREMENT = 7;                // 上报数据需求项，供平台端通知。Body的PB模型为TSLIOTNeedDataRequest
    public static final short CMD_TYPE_PERSON_INFO_ERROR = 8;                 // 上报设备端使用人员数据时的错误信息。Body的PB模型为TSLIOTPersonError

    // 云到端的请求命令
    public static final short CMD_TYPE_DISPATCH_BLACK = (short)0x8002;        // 下发兼通知黑名单。Body的PB模型为 TSLIOTDispatchPersonListRequest
    public static final short CMD_TYPE_DISPATCH_ONCE_PWD = (short)0x8003;     // 下发一次性密码。Body的PB模型为TSLIOTIssueOncePWDRequest
    public static final short CMD_TYPE_DISPATCH_PERSON = (short)0x8004;       // 下发兼通知人员信息。Body的PB模型为 TSLIOTDispatchPersonListRequest
    public static final short CMD_TYPE_DISPATCH_ROOM_INFO = (short)0x8005;    // 下发兼通知房间信息。Body的PB模型为 TSLIOTDispatchRoomInfoRequest
    public static final short CMD_TYPE_DISPATCH_MAKE_CARD = (short)0x8006;    // 下发兼通知制卡信息。Body的PB模型为 TSLIOTDispatchCardListRequest

    public static class SyncDataType{
        public static String SYNC_DATA_TYPE_PERSON = "person";                  //人员信息
        public static String SYNC_DATA_TYPE_PERSON_USER = "user";                      //用户信息
        public static String SYNC_DATA_TYPE_PERSON_VISTOR = "visitor";                  //访客信息

        public static String SYNC_DATA_TYPE_BLACK = "black";                    //黑名单信息
        public static String SYNC_DATA_TYPE_MAKE_CARD = "make_card";            //发卡数据
        public static String SYNC_DATA_TYPE_ROOM_INFO = "room_info";            //房间信息

    }

    /***
     * 通用业务:
     */
    // 云到端的请求命令
    public static final short CMD_TYPE_PASS_RULE = (short)0x8003;            // 平台下发通行规则，Body的PB模型为 TSLIOTPassRule
    public static final short CMD_TYPE_UPDATE_TIME_ACK = (short)0x8004;      // 校时返回时间戳，Body的PB模型为TSLIOTTimeResult
    public static final short CMD_TYPE_UPGRADE_TASK = (short)0x8001;         // 服务端下发升级通知, Body的PB模型为 TSLIOTUpgradeTask
    public static final short CMD_TYPE_UPGRADE_STATUS_ACK = (short)0x8002;   // 服务端收到设备端升级状态的响应, Body的PB模型为 TSLIOTCommonResult
    public static final short CMD_TYPE_INIT_DATA_REQUEST = (short)0x8006;    // 设备数据初始化请求。设备需清除服务器下发的数据，包括住户信息、黑白名单、一次性密码等。Body为 TSLIOTCommonRequest
    public static final short CMD_TYPE_QR_INFO = (short)0x8007;              // 平台通知设备的二维码的解密秘钥及有效时间规则 Body的PB模型为 TSLIOTQrCodeInfo
    public static final short CMD_TYPE_PLATFORM_SETTING = (short)0x8008;     // 平台下发系统配置 Body的PB模型为TSLIOTPlatformSetting

    // 端到云的请求命令
    public static short CMD_TYPE_UPGRADE_TASK_ACK = 1;                  // 设备端收到升级任务的响应, Body的PB模型为 TSLIOTCommonResult
    public static short CMD_TYPE_UPGRADE_STATUS = 2;            		// 设备升级状态报告。Body的PB模型为 TSLIOTUpgradeStatus
    public static short CMD_TYPE_GET_PASS_RULE = 3;            		    // 请求设备的通行规则(除了人脸--刷卡/密码等)。Body的PB模型为TSLIOTCommonRequest
    public static short CMD_TYPE_UPDATE_TIME = 4;               		// 校时请求。Body为 TSLIOTCommonRequest
    public static short CMD_TYPE_INIT_DATA_ACK = 6;             		// 设备数据初始化请求结果。Body的PB模型为TSLIOTCommonResult
    public static short CMD_TYPE_GET_QR_INFO = 7;                       // 设备端获取二维码的解密秘钥及有效时间规则 Body的PB模型为 TSLIOTCommonRequest
    public static short CMD_TYPE_GET_PLATFORM_SETTING = 8;              // 请求获取平台设备Body TSLIOTCommonRequest

    //服务间的
    public static final short CMD_TYPE_SERVER_WILL = -1;              // 服务离线是广播，方便后期分布式服务部署需求


    /**
     * 业务数据记录：
     */
    // 端到云的请求命令
    public static short CMD_TYPE_UPLOAD_PASS_LOG = 1;                       // 上报设备日志。Body的PB模型为 TSLIOTUploadPassLogRequest
    // 云到端的请求命令
    public static final short CMD_TYPE_UPLOAD_PASS_LOG_ACK = (short)0x8001;       // 上报设备日志结果。Body的PB模型为TSLIOTCommonResult

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
}
