package components;

import Connector.MQTTConnector;
import DAO.*;
import Utilities.Helper;
import model.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataCollector {

    public DataCollector() {
        MQTTConnector mqttConnector;
        String topic = "/iot_agriculture/#";
        mqttConnector = new MQTTConnector();
        mqttConnector.connect();
        mqttConnector.getmMqttClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                if(topic.equals("/iot_agriculture/identify/farm_id/get")){
                    createFarmId(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/identify/farm_id/change")){
                    System.out.println("chang farmId");
                    changeFarmId(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/identify/device_id/set")){
                    setDeviceId(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/status/gateway/online")){
                    System.out.println("gateway online");
                    notifyGatewayOnline(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/status/gateway/offline")){
                    notifyGatewayOffline(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/status/device/online")){
                    notifyDeviceOnline(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/status/devices/offline")){
                    notifyDevicesOffline(mqttMessage);
                }
                if(topic.equals("/iot_agriculture/status/device/offline")){
                    System.out.println("device offline");
                }
                if(topic.equals("/iot_agriculture/monitoring")){
                    observeSensor(mqttMessage);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        mqttConnector.subcribe(topic);
    }
    private static void createFarmId(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long randomNumber;
        String username, password;
        if(jsonObject.get("username") instanceof String){
            username = ((String) jsonObject.get("username"));
        }else return;
        if(jsonObject.get("password") instanceof String){
            password = (String) jsonObject.get("password");
        }else return;
        if(jsonObject.get("randomNumber") instanceof Long){
            randomNumber = ((Long) jsonObject.get("randomNumber"));
        }else return;
        if(!verifyUser(username,password)){
            MQTTConnector mqttConnectorTemp = new MQTTConnector();
            mqttConnectorTemp.connect();
            JSONObject objectReturn = new JSONObject();
            objectReturn.put("farmId",(-1));
            mqttConnectorTemp.publishMessage(objectReturn.toJSONString(),"/iot_agriculture/identify/farm_id/supply/"+randomNumber);
            mqttConnectorTemp.disconnect();
            return;
        }
        FarmDao farmDao = new FarmDao();
        int farmId = farmDao.save(new Farm(null,null,null,true,null));
        JSONObject objectReturn = new JSONObject();
        objectReturn.put("farmId",farmId);
        MQTTConnector mqttConnectorTemp = new MQTTConnector();
        mqttConnectorTemp.connect();
        mqttConnectorTemp.publishMessage(objectReturn.toJSONString(),"/iot_agriculture/identify/farm_id/supply/"+randomNumber);
        mqttConnectorTemp.disconnect();

    }

    private static void changeFarmId(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long newFarmId;
        Long randomNumber;
        String username, password;
        if(jsonObject.get("username") instanceof String){
            username = ((String)jsonObject.get("username"));
        }else return;
        if(jsonObject.get("password") instanceof String){
            password = ((String) jsonObject.get("password"));
        }else return;
        if(jsonObject.get("newFarmId") instanceof Long){
            newFarmId = ((Long)jsonObject.get("newFarmId"));
        }else return;
        if(jsonObject.get("randomNumber") instanceof Long){
            randomNumber = (Long) jsonObject.get("randomNumber");
        }else return;
        System.out.println("verify user");
        if(!verifyUser(username,password)){
            MQTTConnector mqttConnectorTemp = new MQTTConnector();
            mqttConnectorTemp.connect();
            JSONObject objectReturn = new JSONObject();
            objectReturn.put("farmId",(-1));
            mqttConnectorTemp.publishMessage(objectReturn.toJSONString(),"/iot_agriculture/identify/farm_id/response_change/"+randomNumber);
            mqttConnectorTemp.disconnect();
            return;
        }
        MQTTConnector mqttConnector = new MQTTConnector();
        mqttConnector.connect();
        FarmDao farmDao = new FarmDao();
        Farm newFarm = farmDao.getById(newFarmId.intValue());
        if(newFarm==null || newFarm.getStatus()==true){
            JSONObject jsonObjectResponse = new JSONObject();
            jsonObjectResponse.put("farmId",0);
            String topicResponse = "/iot_agriculture/identify/farm_id/response_change/"+randomNumber;
            mqttConnector.publishMessage(jsonObjectResponse.toJSONString(),topicResponse);
            mqttConnector.disconnect();
        }else{
            JSONObject jsonObjectResponse = new JSONObject();
            jsonObjectResponse.put("farmId",newFarmId);
            String topicResponse = "/iot_agriculture/identify/farm_id/response_change/"+randomNumber;
            mqttConnector.publishMessage(jsonObjectResponse.toJSONString(),topicResponse);
            mqttConnector.disconnect();
        }
    }

    private static boolean verifyUser(String username, String password){
        MQTTConnector mqttConnector = new MQTTConnector();
        mqttConnector.connect();
        UserDao userDao = new UserDao();
        User user = userDao.getByUsernameAndPassword(username,password);
        if(user == null){
            return false;
        }else{
            return true;
        }
    }

    private static void setDeviceId(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long deviceId=null;
        if(jsonObject.get("deviceId") instanceof Long){
            deviceId = (Long)jsonObject.get("deviceId");
        }else return;

        DeviceDao deviceDao = new DeviceDao();
        deviceDao.save(new Device(deviceId,null,null,true,null));
    }

    private static void notifyGatewayOffline(MqttMessage mqttMessage){
        Integer farmId = Helper.mqttMessageToInteger(mqttMessage);
        System.out.println("Gateway "+farmId+" offline");
        DeviceDao deviceDao = new DeviceDao();
        List<Device> devices = deviceDao.getByFarmId(farmId);
        System.out.println(devices.size());
        for(Device device: devices){
            System.out.println("deviceID offline: "+device.getDeviceID());
            Device newDevice = new Device(device);
            newDevice.setStatus(Boolean.FALSE);
            deviceDao.update(device,newDevice);
        }
        FarmDao farmDao = new FarmDao();
        Farm farm=farmDao.getById(farmId);
        Farm newFarm = new Farm(farm);
        newFarm.setStatus(Boolean.FALSE);
        farmDao.update(farm,newFarm);
    }

    private static void notifyGatewayOnline(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long farmId=null;
        if(jsonObject.get("farmId") instanceof Long){
            farmId = (Long)jsonObject.get("farmId");
        }else return;
        FarmDao farmDao = new FarmDao();
        System.out.println("farmId: "+farmId);
        Farm oldFarm = farmDao.getById(farmId.intValue());
        Farm newFarm = new Farm(oldFarm);
        newFarm.setStatus(Boolean.TRUE);
        farmDao.update(oldFarm,newFarm);
    }

    private static void notifyDeviceOffline(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long deviceId=null;
        if(jsonObject.get("deviceId") instanceof Long){
            deviceId = (Long)jsonObject.get("deviceId");
        }else return;
        DeviceDao deviceDao = new DeviceDao();
        Device oldDevice = deviceDao.getById(deviceId.intValue());
        Device newDevice = new Device(oldDevice);
        newDevice.setStatus(Boolean.FALSE);
        deviceDao.update(oldDevice,newDevice);
    }

    private static void notifyDevicesOffline(MqttMessage mqttMessage){
        System.out.println("Devices offline");
        JSONArray jsonArray = Helper.mqttMessageToJsonArray(mqttMessage);
        DeviceDao deviceDao = new DeviceDao();
        for(Object deviceId: jsonArray){
            System.out.println("deviceID offline: "+(Long)deviceId);
            Device oldDevice = deviceDao.getById((Long)deviceId);
            Device newDevice = new Device(oldDevice);
            newDevice.setStatus(Boolean.FALSE);
            deviceDao.update(oldDevice,newDevice);
        }
    }

    private static void notifyDeviceOnline(MqttMessage mqttMessage){
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long deviceId=null;
        if(jsonObject.get("deviceId") instanceof Long){
            deviceId = (Long)jsonObject.get("deviceId");
        }else return;
        System.out.println("device "+ deviceId +" online");
        final DeviceDao deviceDao = new DeviceDao();
        Device device = deviceDao.getById(deviceId);
        if(device == null){
            final Long finalDeviceId = deviceId;
            final Integer farmId = Helper.convertDeviceIdToFarmId(deviceId);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer plotId;
                    PlotDao plotDao = new PlotDao();
                    List<Plot> plots = new ArrayList<Plot>();
                    do {
                        plots = plotDao.getByFarmId(farmId);
                        System.out.println("PlotIds in Farm "+ farmId);
                        for(Plot plot : plots){
                            System.out.println(plot.toString());
                        }
                        System.out.println("Enter plotId for device " + finalDeviceId +" (enter <=0 to skip)");
                        Scanner scanner = new Scanner(System.in);
                        plotId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("plotId: "+plotId);
                        if(plotId<=0) break;
                        Boolean isEqual = Boolean.FALSE;
                        for(Plot plot:plots){
                            System.out.println("plotId: "+plot.getPlotID());
                            if(plotId == plot.getPlotID()) {
                                isEqual=Boolean.TRUE;
                                break;
                            }
                        }
                        if(isEqual==Boolean.TRUE) break;
                        System.out.println("PlotId don't exist! Retry!");
                    } while (Boolean.TRUE);
                    deviceDao.save(new Device(finalDeviceId, null, null, Boolean.TRUE, plotId));
                }
            });
            thread.start();
        }else{
            Device newDevice = new Device(device);
            newDevice.setStatus(Boolean.TRUE);
            deviceDao.update(device,newDevice);
        }

    }

    private static void observeSensor(MqttMessage mqttMessage){
        System.out.println("received data sensor!");
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        System.out.println(jsonObject.toJSONString());
        Double airTemperature=null, airHumidity=null, soilTemperature=null, soilMoisture=null;
        Long lightLevel=null,deviceId=null;
        if(jsonObject.get("airTemperature") instanceof Double){
//            System.out.println("airTemperature");
            airTemperature = (Double) jsonObject.get("airTemperature");
        }
        if(jsonObject.get("airHumidity") instanceof Double){
//            System.out.println("airHumidity");
            airHumidity = (Double) jsonObject.get("airHumidity");
        }
        if((jsonObject.get("soilTemperature") instanceof Double)){
//            System.out.println("soilTemperature");
            soilTemperature = (Double) jsonObject.get("soilTemperature");
        }
        if(jsonObject.get("soilMoisture") instanceof Double){
//            System.out.println("soilMoisture");
            soilMoisture = (Double) jsonObject.get("soilMoisture");
        }
        if(jsonObject.get("lightLevel") instanceof Long){
//            System.out.println("lightLevel");
            lightLevel = (Long) jsonObject.get("lightLevel");
        }
        if(jsonObject.get("deviceId") instanceof Long){
//            System.out.println("deviceId");
            deviceId = (Long) jsonObject.get("deviceId");
        }else return;
        SensingDao sensingDao = new SensingDao();
        DeviceDao deviceDao = new DeviceDao();
        Integer plotId = deviceDao.getById(deviceId).getPlotID();
        Sensing sensing = new Sensing(
                deviceId,
                plotId,
                (soilMoisture)!=null?soilMoisture.floatValue():null,
                (soilTemperature)!=null?soilTemperature.floatValue():null,
                (airHumidity)!=null?airHumidity.floatValue():null,
                (airTemperature)!=null?airTemperature.floatValue():null,
                (lightLevel)!=null?lightLevel.intValue():null,
                Helper.getNow()
        );
        sensingDao.save(sensing);
    }
}
