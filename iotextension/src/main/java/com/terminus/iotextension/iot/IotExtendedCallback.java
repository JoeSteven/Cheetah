package com.terminus.iotextension.iot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author rain
 * @date 2019/4/30
 */
public interface IotExtendedCallback extends MqttCallbackExtended {
    @Override
    void connectComplete(boolean reconnect, String serverURI);

    @Override
    void connectionLost(Throwable cause);

    @Override
    void messageArrived(String topic, MqttMessage message) throws Exception;

    @Override
    void deliveryComplete(IMqttDeliveryToken token);
}
