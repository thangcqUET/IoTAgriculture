import Connector.DBConnector;
import Connector.MQTTConnector;
import DAO.*;
import Utilities.Helper;
import model.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String host=null;
        if(args.length!=0){
            host = args[0];
        }
        System.out.println(host);

        String[] topics = new String[]{"/registry/HGW",
                "/registry/device",
                "/registry/plot",
                "/status/HGW/offline",
                "/status/HGW/online",
                "/status/device/offline",
                "/status/device/online",
                "/observing"
        };

        List<MQTTConnector> mqttConnectors = new ArrayList<MQTTConnector>();
        for(int i=0;i<topics.length;i++){
            if(host!=null){
                mqttConnectors.add(new MQTTConnector(host));
            }else{
                mqttConnectors.add(new MQTTConnector());
            }
        }
        MQTTConnector mqttConnectorTemp;
        for(String topic: topics){
            mqttConnectorTemp=mqttConnectors.iterator().next();
            mqttConnectorTemp.connect();
            mqttConnectorTemp.subcribe(topic);
            mqttConnectorTemp.getmMqttClient().setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {

                }

                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    if(topic.equals("/registry/HGW")){

                    }else if(topic.equals("/registry/device")){

                    }else if(topic.equals("/registry/plot")){

                    }else if(topic.equals("/status/HGW/offline")){

                    }else if(topic.equals("/status/HGW/online")){

                    }else if(topic.equals("/status/device/offline")){

                    }else if(topic.equals("/status/device/online")){

                    }else if(topic.equals("/observing")){

                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        }
    }

    public static<T> void printAll(List<T> ts){
        for(int i=0;i<ts.size();i++){
            System.out.println(ts.get(i).toString());
        }
    }
}
