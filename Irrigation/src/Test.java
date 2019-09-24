import Connector.DBConnector;
import Connector.MQTTConnector;
import DAO.*;
import Utilities.Helper;
import model.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test {
    private static String host=null;
    public static void main(String[] args) {
        if(args.length!=0){
            host = args[0];
        }

        String[] topics = new String[]{"registry/HGW",
                "registry/device",
                "registry/plot",
                "status/HGW/offline",
                "status/HGW/online",
                "status/device/offline",
                "status/device/online",
                "observing",
                "test"
        };

        List<MQTTConnector> mqttConnectors = new ArrayList<MQTTConnector>();
        for(int i=0;i<topics.length;i++){
            if(host==null){
                mqttConnectors.add(new MQTTConnector());
            }else{
                mqttConnectors.add(new MQTTConnector(host));
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
                    if(topic.equals("registry/HGW")){
                        registryHomeGateWay(mqttMessage);
                    }else if(topic.equals("registry/device")){
                        registryDevice(mqttMessage);
                    }else if(topic.equals("status/HGW/offline")){
                        notifyHomegatewayOffline(mqttMessage);
                    }else if(topic.equals("status/HGW/online")){
                        notifyHomegatewayOnline(mqttMessage);
                    }else if(topic.equals("status/device/offline")){
                        notifyDeviceOffline(mqttMessage);
                    }else if(topic.equals("status/device/online")){
                        notifyDeviceOnline(mqttMessage);
                    }else if(topic.equals("observing")){
                        observeSensor(mqttMessage);
                    }else if(topic.equals("test")){
                        test(mqttMessage);
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

    public static void registryHomeGateWay(MqttMessage mqttMessage){


        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);

        int userID;
        String macAddress;
        if(jsonObject.get("userId") instanceof Long){
            userID= ((Long)jsonObject.get("userId")).intValue();
        }else return;

        if(jsonObject.get("mac") instanceof String){
            macAddress = ((String) jsonObject.get("mac"));
        }else return;


        FarmDao farmDao = new FarmDao();

        int farmId = farmDao.save(new Farm(null,null,null,userID));
        JSONObject objectReturn = new JSONObject();
        objectReturn.put("farmId",farmId);

        MQTTConnector mqttConnectorTemp = new MQTTConnector(host);
        mqttConnectorTemp.connect();
        mqttConnectorTemp.publishMessage(objectReturn.toJSONString(),"supply/"+macAddress);
        mqttConnectorTemp.disconnect();

    }


    public static void registryDevice(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long deviceId=null;
        if(jsonObject.get("deviceId") instanceof Long){
            deviceId = (Long)jsonObject.get("deviceId");
        }else return;

        DeviceDao deviceDao = new DeviceDao();
        deviceDao.save(new Device(deviceId,null,null,null));
    }

    public static void notifyHomegatewayOffline(MqttMessage mqttMessage){
        System.out.println(mqttMessage.toString());
    }

    public static void notifyHomegatewayOnline(MqttMessage mqttMessage){

    }

    public static void notifyDeviceOffline(MqttMessage mqttMessage){

    }

    public static void notifyDeviceOnline(MqttMessage mqttMessage){

    }

    public static void observeSensor(MqttMessage mqttMessage){

    }

    public static void test(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        System.out.println(jsonObject.get("ten"));
    }
}
