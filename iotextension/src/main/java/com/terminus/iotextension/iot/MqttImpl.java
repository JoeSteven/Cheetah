package com.terminus.iotextension.iot;

import com.terminus.iot.IoTClient;
import com.terminus.iotextension.event.AeskeyEvent;
import com.terminus.iotextension.event.BaseEvent;
import com.terminus.iotextension.iot.config.DataType;
import com.terminus.iotextension.iot.config.DevStatus;
import com.terminus.iotextension.iot.config.Direction;
import com.terminus.iotextension.iot.config.NetType;
import com.terminus.iotextension.iot.config.OpenStatus;
import com.terminus.iotextension.iot.config.OpenType;
import com.terminus.iotextension.iot.config.PersonType;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

/**
 * @author rain
 * @date 2019/4/26
 */
public abstract class MqttImpl {

    final static String TAG = "MQTT";

    IotMessageCallback<BaseEvent> mIotMessageCallback;
    IoTClient mIoTClient;

    abstract IoTClient initClient();

    abstract void connect();

    abstract void disConnect();

    abstract void disConnectForce();

    abstract void reConnect();

    abstract void uploadNetInfo(NetType netType, String netName, String outIp, String innerIp,
                                String mask, String gateWay, String dns1, String dns2);

    abstract void uploadNeedInfo(DataType... type);

    abstract void close();

    abstract void requestQr();

    abstract void requestRule();

    abstract void requestSetting();

    abstract void requestTime();

    abstract void errorInfo(int personId,PersonType type,long version,String customInfo);

    abstract void asylocalData(DataType type, long version);

    abstract void uploadPassLog(int personId, String feature, PersonType personType,
                                Direction direction, long time, OpenStatus openStatus,
                                DevStatus devStatus, OpenType openType, String cardNo,
                                String imgUrl, String videoUrl,String reserve,long logId);

    void send(String topic, int sequenceId, byte[] data) throws MqttException {
        if (check()) {
            mIoTClient.send(topic, sequenceId, data);
        }
    }

    boolean check() {
        return mIoTClient != null;
    }

    public interface IotMessageCallback<T> {
        void onError(Throwable throwable);

        void onSuccess(boolean reconnect, boolean regis);

        void onEvent(T t);
    }
}
