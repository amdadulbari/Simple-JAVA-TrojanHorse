package handlers;

import constants.SettingsConstants;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnectionHandler {
    private static MqttConnectionHandler mqttConnectionHandler;
    private MqttClient client;

    public static MqttConnectionHandler getInstance() {
        if (mqttConnectionHandler == null) {
            mqttConnectionHandler = new MqttConnectionHandler();
        }
        return mqttConnectionHandler;
    }

    public boolean connect() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(false);

            client = new MqttClient(SettingsConstants.MQ_URL, MqttClient.generateClientId(), new MemoryPersistence());
            client.connect(options);
            client.subscribe(SettingsConstants.MQ_SUBSCRIBE_TOPIC);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttMessage.setQos(0);
        mqttMessage.setRetained(false);

        try {
            client.publish(SettingsConstants.MQ_PUBLISH_TOPIC, mqttMessage);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String message,String topic){
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttMessage.setQos(0);
        mqttMessage.setRetained(false);

        try {
            client.publish(topic, mqttMessage);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void addCallback(MqttCallback callback) {
        client.setCallback(callback);
    }

}