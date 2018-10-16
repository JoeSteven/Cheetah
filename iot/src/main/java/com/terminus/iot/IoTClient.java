package com.terminus.iot;

import android.text.TextUtils;

import com.terminus.iot.msg.IotFrame;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */
public class IoTClient {
    private Config config;
    private MqttClient realClient;
    private IoTCallback callback;
    private volatile short sequenceId;


    public static Config with(String serverAddress) {
        return new Config(serverAddress);
    }

    public static IoTClient create(Config config) {
        config.check();
        return new IoTClient(config);
    }

    private IoTClient(Config config) {
        this.config = config;
    }

    public <T extends MqttCallback>void connect(T callback) throws MqttException {
        if (realClient == null) {
            realClient = new MqttClient(config.serverAddress, config.clientId, new MemoryPersistence());
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(config.cleanSession);
        options.setUserName(config.userName);
        options.setPassword(config.password);
        options.setConnectionTimeout(config.connectionTimeout);
        options.setKeepAliveInterval(config.keepAliveInterval);
        options.setAutomaticReconnect(config.autoReconnect);

        realClient.setCallback(callback);

        if (!TextUtils.isEmpty(config.willTopic)) {
            byte[] willByte = newFrame(IoTProtocol.MSG_TYPE_SYSTEM,
                    generateId(),
                    IoTProtocol.SERVICE_TYPE_ACCESS,
                    IoTProtocol.CMD_TYPE_WILL,
                    new String(config.password).getBytes()).toByte();
            options.setWill(config.willTopic, willByte, 0, false);
        }
        realClient.connect(options);
    }

    public void subscribe(String[] topics, int[] qos) throws MqttException {
        realClient.subscribe(topics, qos);
    }

    public boolean isConnected() {
        return realClient != null && realClient.isConnected();
    }

    public void send(String sendTopic, short sequenceId, byte[] data) throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setId(sequenceId);
        message.setQos(2);
        message.setRetained(false);
        message.setPayload(data);
        if (realClient != null && realClient.isConnected()) {
            realClient.publish(sendTopic, message);
        }
    }

    public void reconnect() throws MqttException {
        realClient.reconnect();
    }

    public void disconnect() throws MqttException {
        if (realClient != null && realClient.isConnected()) {
            realClient.disconnect();
        }
    }

    public MqttClient mqttClient() {
        return realClient;
    }

    public IotFrame newFrame(byte msgType, short  sequenceId, short  serviceType, short cmd, byte[] body) {
        return new IotFrame(msgType, sequenceId, serviceType, cmd, body)
                .setRsaKey(config.rsaKey)
                .setAesKey(config.aesKey);
    }
    
    public IotFrame newFrame() {
        return new IotFrame()
                .setRsaKey(config.rsaKey)
                .setAesKey(config.aesKey);
    }

    public synchronized short generateId() {
        if (sequenceId > Short.MAX_VALUE) {
            sequenceId = 0;
        } else {
            sequenceId++;
        }
        return sequenceId;
    }

    public static class Config{
        String serverAddress;
        String userName;
        String devId;
        String clientId;
        char[] password;
        int connectionTimeout = 20;
        int keepAliveInterval = 65;
        boolean autoReconnect = true;
        boolean cleanSession = false;
        String willTopic;
        String rsaKey;
        byte[] aesKey;

        public Config(String serverAddress) {
            this.serverAddress = serverAddress;
        }

        public Config userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Config devId(String devId) {
            this.devId = devId;
            return this;
        }

        public Config clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Config password(char[] password) {
            this.password = password;
            return this;
        }

        public Config connectionTimeout(int timeoutSeconds) {
            if (timeoutSeconds > 0) {
                this.connectionTimeout = timeoutSeconds;
            }
            return this;
        }

        public Config keepAliveInterval(int interval) {
            if (interval > 0) {
                this.keepAliveInterval = interval;
            }
            return this;
        }

        public Config autoReconnect(boolean autoReconnect) {
            this.autoReconnect = autoReconnect;
            return this;
        }

        public Config cleanSession(boolean cleanSession) {
            this.cleanSession = cleanSession;
            return this;
        }

        public Config willTopic(String willTopic) {
            this.willTopic = willTopic;
            return this;
        }
        
        public Config rsaKey(String rsaKey) {
            this.rsaKey = rsaKey;
            return this;
        }
        
        public Config aesKey(byte[] aesKey) {
            this.aesKey = aesKey;
            return this;
        }
 

        public IoTClient create() {
            check();
            return new IoTClient(this);
        }

        private void check() {
            if (TextUtils.isEmpty(serverAddress)
                    || TextUtils.isEmpty(userName)
                    || TextUtils.isEmpty(devId)
                    || TextUtils.isEmpty(clientId)
                    || password == null
                    || TextUtils.isEmpty(rsaKey)
                    || aesKey == null) {
                throw new IllegalArgumentException("Config must be complete with serverAddress, userName, devId, clientId, password, rsaKey, aesKey!");
            }
        }
    }

}
