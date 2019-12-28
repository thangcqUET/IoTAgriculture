package Connector;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class MQTTConnector {
    private String host = "localhost";

    public MqttClient getmMqttClient() {
        return mMqttClient;
    }

    private MqttClient mMqttClient;
    private MqttConnectOptions mqttConnectOptions;
    public MQTTConnector(String host){
        mqttConnectOptions = new MqttConnectOptions();
        this.host = host;
        mqttConnectOptions.setUserName("admin");
        mqttConnectOptions.setPassword("12345678".toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
    }
    public MQTTConnector(){
        mqttConnectOptions = new MqttConnectOptions();
//        this.host = "iotagriculture.ddns.net";
        mqttConnectOptions.setUserName("admin");
        mqttConnectOptions.setPassword("12345678".toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        byte[] payloadOffline = {0,1};
        mqttConnectOptions.setWill("/status/HGW/offline",payloadOffline,2,false);
    }
    public MQTTConnector(String host, MqttConnectOptions mqttConnectOptions){
        this.host= host;
        this.mqttConnectOptions=mqttConnectOptions;
    }

    public void connect(){
        try {
            MqttClientPersistence mqttClientPersistence = new MqttDefaultFilePersistence("paho");
            mMqttClient = new MqttClient("tcp://"+this.host+":1883",MqttClient.generateClientId(),mqttClientPersistence);
            mMqttClient.connect(mqttConnectOptions);
        } catch (MqttException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void disconnect(){
        try {
            mMqttClient.disconnect();
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void publishMessage(String message, String topic){
        try {
            if(message == null){
                //System.out.println("null String ");
                Thread.sleep(3000);
                return;
            }

            MqttMessage messageMqtt = new MqttMessage();
            messageMqtt.setPayload(message.getBytes());
            messageMqtt.setQos(2);
            System.out.println(">>message is published in topic '"+topic+"' : ");
            System.out.println(messageMqtt);
            mMqttClient.publish(topic, messageMqtt);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subcribe(String topic){
        try {
            mMqttClient.subscribe(topic);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
