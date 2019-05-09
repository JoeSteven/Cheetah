package com.terminus.iotextension.iot;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.terminus.iotextension.iot.config.DataType;
import com.terminus.iotextension.iot.config.DevStatus;
import com.terminus.iotextension.iot.config.Direction;
import com.terminus.iotextension.iot.config.OpenStatus;
import com.terminus.iotextension.iot.config.OpenType;
import com.terminus.iotextension.iot.config.PersonType;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTBusinessLog;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDataSync;
import com.terminus.iotextension.mqtt.protobuf.TSLIOTDevice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author rain
 * @date 2019/4/30
 */
class IotPBUtil {

    //设备信息注册body
    static byte[] constructDeviceInfo(){
        TSLIOTDevice.TSLIOTRegisterDeviceRequest.Builder deviceInfoBuilder = TSLIOTDevice.TSLIOTRegisterDeviceRequest.newBuilder();
        deviceInfoBuilder.setDevId(IoTConstant.DEV_ID);
        deviceInfoBuilder.setSn(IoTConstant.SN);
        deviceInfoBuilder.setDevName(IoTConstant.DEVNAME);
        deviceInfoBuilder.setFatherDevId(IoTConstant.FATHER_ID);
        deviceInfoBuilder.setCategory(IoTConstant.CATEGORY);
        deviceInfoBuilder.setType(IoTConstant.TYPE);
        deviceInfoBuilder.setModel(IoTConstant.MODEL);
        deviceInfoBuilder.setMac(IoTConstant.MAC);
        deviceInfoBuilder.setSoftwareVersion(IoTConstant.SOFTWARE_VERSION);
        deviceInfoBuilder.setHardwareVersion(IoTConstant.HARDWARE_VERSION);
        deviceInfoBuilder.setCustomInfo(IoTConstant.TYPE);

        if (IoTConstant.FUNCTIONS != null) {
            if (!IoTConstant.FUNCTIONS.isEmpty()) {
                for(Integer func: IoTConstant.FUNCTIONS)
                    deviceInfoBuilder.addFunctions(func);
            }
        }

        deviceInfoBuilder.setShellSupport(false);
        return getBytes(deviceInfoBuilder.build());

    }

    // 生成更新 aeskey 的请求body
    static byte[] generateUpdateAESKey(byte[] key) {
        TSLIOTDevice.TSLIOTUpdateAESKeyRequest.Builder aesUpdateBuilder = TSLIOTDevice.TSLIOTUpdateAESKeyRequest.newBuilder();
        ByteString aa = ByteString.copyFrom(key);
        aesUpdateBuilder.setAesKey(aa);
        return getBytes(aesUpdateBuilder.build());
    }

    /**
     * 设备端上报本地数据最新版本
     * @param type person(user/visitor)、black、make_card、room_info
     * @param data_version 本地当前数据类型端最新版本号,初始为0
     * @return PB
     */
    static byte[] constructPullDataSync(DataType type, long data_version){

        TSLIOTDataSync.TSLIOTPullDataRequest.Builder pullDataRequest= TSLIOTDataSync.TSLIOTPullDataRequest.newBuilder();
        pullDataRequest.setDevId(IoTConstant.DEV_ID);
        pullDataRequest.setDataType(type.value());
        pullDataRequest.setDataVersion(data_version);
        return getBytes(pullDataRequest.build());
    }


    /**
     * 提交设备需求
     * 数据内容为:person、black、make_card、room_info
     */
    static byte[] constructNeedDataSync(List<DataType> items){

        TSLIOTDataSync.TSLIOTNeedDataRequest.Builder needDataRequest= TSLIOTDataSync.TSLIOTNeedDataRequest.newBuilder();
        needDataRequest.setDevId(IoTConstant.DEV_ID);

        for(DataType func: items)
            needDataRequest.addNeedItems(func.value());
        return getBytes(needDataRequest.build());

    }

    /**
     * 通行日志上报,feature采用json来进行描述
     * @return PB
     */
    static byte[] constructPassLog(int personId, String feature, PersonType personType,
                                   Direction direction, long time, OpenStatus openStatus,
                                   DevStatus devStatus, OpenType openType, String cardNo,
                                   String imgUrl, String videoUrl){
        TSLIOTBusinessLog.TSLIOTUploadPassLogRequest.Builder logBuilder =
                TSLIOTBusinessLog.TSLIOTUploadPassLogRequest.newBuilder();
        TSLIOTBusinessLog.TSLIOTUploadPassLogRequest.TSLIOTPassLog log =
                TSLIOTBusinessLog.TSLIOTUploadPassLogRequest.TSLIOTPassLog
                        .newBuilder()
                        .setDevId(IoTConstant.DEV_ID)
                        .setPersonId(personId)
                        .setFeature(feature)
                        .setPersonType(personType.value())
                        .setDirection(direction.value())
                        .setTime(time)
                        .setOpenResult(openStatus.value())
                        .setDevStatus(devStatus.value())
                        .setOpenType(openType.value())
                        .setCardNo(cardNo)
                        .setPassImg(imgUrl)
                        .setPassVideo(videoUrl)
                        .build();
                logBuilder.addPassLogs(log);

        return getBytes(logBuilder.build());
    }

    private static byte[] getBytes(GeneratedMessageV3 pb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            pb.writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toByteArray();
    }
}
