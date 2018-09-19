package com.terminus.iot;

import android.support.annotation.NonNull;

import com.terminus.iot.msg.IMsgParser;
import com.terminus.iot.msg.IotFrame;
import com.terminus.iot.msg.IotMsgParser;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;



/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */
public abstract class IoTCallback implements MqttCallback{
    private IMsgParser parser;
    private IoTClient client;

    public IoTCallback(IoTClient client) {
       this(client, new IotMsgParser());
    }

    public IoTCallback(IoTClient client, IMsgParser parser) {
        this.parser = parser;

    }

    @Override
    public void connectionLost(Throwable cause) {
        // disconnected, try reconnect
        try {
            client.reconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (message.getPayload() == null || message.getPayload().length <= 0) return;
        messageArrived(topic, parser.parse(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    protected abstract void messageArrived(@NonNull String topic, @NonNull IotFrame frame);
}
