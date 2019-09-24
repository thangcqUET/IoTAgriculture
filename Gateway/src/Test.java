import Connector.MQTTConnector;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.*;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

        MQTTConnector mqttConnector = new MQTTConnector("42.112.105.4");

        mqttConnector.connect();

        JSONObject jsonObject = new JSONObject();
        Long deviceId = new Long(123456);
        jsonObject.put("deviceId",deviceId);

        mqttConnector.publishMessage(jsonObject.toJSONString(),"registry/device");
//        mqttConnector.subcribe("registry/");
//        mqttConnector.getmMqttClient().setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean b, String s) {
//
//            }
//
//            @Override
//            public void connectionLost(Throwable throwable) {
//
//            }
//
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                System.out.println(mqttMessage.toString());
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//
//            }
//        });
    }
}
