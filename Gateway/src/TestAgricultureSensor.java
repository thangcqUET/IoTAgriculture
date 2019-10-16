import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import Connector.DataTransfer;
import Connector.DeviceManager;
import Connector.MQTTConnector;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.sensor.AgricultureSensor;
import com.sonycsl.echo.eoj.device.sensor.HumiditySensor;
import com.sonycsl.echo.eoj.device.sensor.TemperatureSensor;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;
import com.sonycsl.echo.processing.defaults.DefaultController;
import dataStructure.GatewayInformation;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.simple.JSONObject;
import utilites.Helper;

import javax.swing.plaf.SplitPaneUI;

import static java.security.CryptoPrimitive.MAC;

public class TestAgricultureSensor {
    public static void main(String[] args) {
        initiateGatewayState();

//        try {
//            GatewayInformation.getInstance();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        setup1();
    }

    public static void startEchonetLiteNetwork(){
        try {
            Echo.start(new DefaultNodeProfile(), new DeviceObject[]{new DefaultController()});
            Thread devicesManager = new Thread(new DeviceManager(10000));
            Thread packetTransfer = new Thread(new DataTransfer(5000));
            devicesManager.start();
            packetTransfer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initiateGatewayState(){
        MQTTConnector mqttConnectorSub = new MQTTConnector();
        mqttConnectorSub.connect();
        mqttConnectorSub.subcribe("/iot_agriculture/#");
        try {
            GatewayInformation gatewayInformation = GatewayInformation.getInstance();
            System.out.println(gatewayInformation.getFarmId());
            System.out.println("Information found!");
            int chose = chooseWhenHaveInformation();

            switch (chose){
                case 1:{
                    Helper.deleteFile("GatewayInformation.dat");
                    createNewFarm(mqttConnectorSub);
                    break;
                }
                case 2:{
                    replaceOldGateway(mqttConnectorSub);
                    break;
                }
                case 3:{
                    try {
                        MQTTConnector mqttConnectorSetWill = new MQTTConnector("/iot_agriculture/status/gateway/offline",GatewayInformation.getInstance().getFarmId());
                        mqttConnectorSetWill.connect();
                        MQTTConnector mqttConnectorPub = new MQTTConnector();
                        mqttConnectorPub.connect();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("farmId",gatewayInformation.getFarmId());
                        mqttConnectorPub.publishMessage(jsonObject.toJSONString(),"/iot_agriculture/status/gateway/online");
                        mqttConnectorPub.disconnect();
                        useNormal(mqttConnectorSub);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Information not found!");
            int chose = chooseWhenDoNotHaveInformation();
            switch (chose){
                case 1:{
                    createNewFarm(mqttConnectorSub);
                    break;
                }
                case 2:{
                    replaceOldGateway(mqttConnectorSub);
                    break;
                }
            }
        }
    }


    private static int chooseWhenHaveInformation(){
        Console inputConsole = System.console();
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Choose a mode: \n" +
                "1. Create new farm \n" +
                "2. Replace old gateway \n" +
                "3. Use normal \n" +
                "Choose: (1, 2 or 3)");
        int chose;
        do{
            chose = inputScanner.nextInt();
            inputScanner.nextLine();
            if(chose !=1&&chose !=2&&chose !=3) System.out.println("Choose: (1, 2 or 3)");
        }while(chose!=1&&chose!=2&&chose!=3);
        return chose;
    }

    private static int chooseWhenDoNotHaveInformation(){
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Choose a mode: \n" +
                "1. Create new farm \n" +
                "2. Replace old gateway \n" +
                "Choose: (1 or 2)");
        int chose;
        do{
            chose = inputScanner.nextInt();
            inputScanner.nextLine();
            if(chose !=1&&chose !=2) System.out.println("Choose: (1 or 2)");
        }while(chose!=1&&chose!=2);
        return chose;
    }

    private static void createNewFarm(MQTTConnector mqttConnectorSub){
        Random rd = new Random();
        Long randomLongTemp = rd.nextLong();
        final Long randomNumber = randomLongTemp>0?randomLongTemp:-randomLongTemp;

        mqttConnectorSub.getmMqttClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception{
                GatewayInformation gatewayInformation = GatewayInformation.getInstance();
                saveFarmIdInCallback(topic,mqttMessage,randomNumber);
                if(topic.equals("/iot_agriculture/identify/farm_id/response_change/"+gatewayInformation.getFarmId())){
                    System.out.println("response_change");
//                    changeFarmId(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/identify/user_id/response_verify/"+gatewayInformation.getFarmId())){
//                    showResult(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/controlling"+gatewayInformation.getFarmId())){
//                    controlDevice(mqttMessage);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        Scanner inputScanner = new Scanner(System.in);
        String username, password;
        System.out.println("Username: ");
        username = inputScanner.nextLine();
        System.out.println("Password: ");
        password = inputScanner.nextLine();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        jsonObject.put("randomNumber",randomNumber);
        MQTTConnector mqttConnectorPub = new MQTTConnector();
        mqttConnectorPub.connect();
        mqttConnectorPub.publishMessage(jsonObject.toString(),"/iot_agriculture/identify/farm_id/get");
        mqttConnectorPub.disconnect();
    }

    private static void replaceOldGateway(MQTTConnector mqttConnectorSub){
        Random rd = new Random();
        Long randomLongTemp = rd.nextLong();
        final Long randomNumber = randomLongTemp>0?randomLongTemp:-randomLongTemp;
        mqttConnectorSub.getmMqttClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                changFarmIdInCallback(topic,mqttMessage,randomNumber);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        Integer newFarmId;
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("New farmId: ");
        newFarmId = inputScanner.nextInt();
        inputScanner.nextLine();
        String username, password;
        System.out.println("Username: ");
        username = inputScanner.nextLine();
        System.out.println("Password: ");
        password = inputScanner.nextLine();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newFarmId",newFarmId);
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        jsonObject.put("randomNumber",randomNumber);
        MQTTConnector mqttConnectorPub = new MQTTConnector();
        mqttConnectorPub.connect();
        mqttConnectorPub.publishMessage(jsonObject.toString(),"/iot_agriculture/identify/farm_id/change");
        mqttConnectorPub.disconnect();
    }

    private static void useNormal(MQTTConnector mqttConnectorSub)throws FileNotFoundException{
        GatewayInformation gatewayInformation = GatewayInformation.getInstance();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("farmId", gatewayInformation.getFarmId());
        mqttConnectorSub.getmMqttClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                controlDeviceInCallBack(topic, mqttMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        startEchonetLiteNetwork();
    }

    public static void saveFarmIdInCallback(String topic, MqttMessage mqttMessage, Long randomNumber){
        if(!topic.equals("/iot_agriculture/identify/farm_id/supply/"+randomNumber)){
            return;
        }
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long farmId;
        if(jsonObject.get("farmId") instanceof Long) {
            System.out.println("get");
            farmId = ((Long) jsonObject.get("farmId"));
            System.out.println("farmId: " + farmId);
        }else{
            return;
        }
        if(farmId == -1){
            System.out.println("Wrong username or password!");
        }else if(farmId == 0) {
            System.out.println("Error get data!");
        }else{
            GatewayInformation gatewayInformation = null;
            try {
                gatewayInformation = GatewayInformation.getInstance();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            gatewayInformation.setFarmId(farmId.intValue());
        }
    }

    public static void changFarmIdInCallback(String topic, MqttMessage mqttMessage, Long randomNumber){
        if(!topic.equals("/iot_agriculture/identify/farm_id/response_change/"+randomNumber)){
            return;
        }
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long farmId;
        if(jsonObject.get("farmId") instanceof Long) {
            System.out.println("get");
            farmId = ((Long) jsonObject.get("farmId"));
            System.out.println("farmId: " + farmId);
        }else{
            return;
        }
        if(farmId == -1){
            System.out.println("Wrong username or password!");
        }else if(farmId == 0) {
            System.out.println("Error get data!");
        }else{
            GatewayInformation gatewayInformation = null;
            try {
                gatewayInformation = GatewayInformation.getInstance();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            gatewayInformation.setFarmId(farmId.intValue());
        }
    }

    public static void controlDeviceInCallBack(String topic, MqttMessage mqttMessage ){

    }
}
