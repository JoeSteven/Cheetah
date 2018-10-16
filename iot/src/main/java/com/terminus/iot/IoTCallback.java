package com.terminus.iot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;



/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */
public abstract class IoTCallback implements MqttCallback{

    public IoTCallback() {
    }

    @Override
    public void connectionLost(Throwable cause) {
        // disconnected, try reconnect
    }

    @Override
    public abstract void messageArrived(String topic, MqttMessage message) throws Exception;

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
