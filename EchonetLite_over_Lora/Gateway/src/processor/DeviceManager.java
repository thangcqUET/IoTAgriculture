package processor;

import Connector.MQTTConnector;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.node.EchoNode;
import dataStructure.DevicesList;
import dataStructure.GatewayInformation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import utilites.Helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DeviceManager implements Runnable{
    DevicesList devicesList;
    //should be explained
    HashMap<EchoNode,Byte> devicesOfflineBeforeTurnOffAll;
    HashMap<EchoNode,Byte> devicesOnlineBeforeTurnOffAll;
    HashMap<EchoNode, Boolean> stateDevicesBeforeTurnOffAll;
    int timeToWaitUpdateDevices;
    int timeToReUpdateDevices;

    public DeviceManager(){
        timeToWaitUpdateDevices = 10000;
        devicesList = DevicesList.getInstance();
    }

    public DeviceManager(int timeToWaitUpdateDevices, int timeToReUpdateDevices) {
        this.timeToWaitUpdateDevices = timeToWaitUpdateDevices;
        this.timeToReUpdateDevices = timeToReUpdateDevices;
        devicesList = DevicesList.getInstance();
        devicesOfflineBeforeTurnOffAll = new HashMap<EchoNode,Byte>();
        devicesOnlineBeforeTurnOffAll = new HashMap<EchoNode,Byte>();
    }

    @Override
    public void run() {
        Echo.addEventListener(new Echo.EventListener(){
            @Override
            public void onNewEchoObject(EchoObject eoj) {
                super.onNewEchoObject(eoj);
                System.out.println("New echo object: "+eoj);
                devicesList.add(eoj.getNode().getAddress());
                stateDevicesBeforeTurnOffAll.put(eoj.getNode(),Boolean.FALSE);
            }

            @Override
            public void onFoundEchoObject(EchoObject eoj) {
                super.onFoundEchoObject(eoj);
//                System.out.println("Found echo object: "+eoj);
                try {
                    devicesList.turnOn(eoj.getNode().getAddress());
                    if(!stateDevicesBeforeTurnOffAll.get(eoj.getNode())){
                        MQTTConnector mqttConnector = new MQTTConnector();
                        mqttConnector.connect();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            GatewayInformation gatewayInformation = GatewayInformation.getInstance();
//                            jsonObject.put("deviceId", Helper.convertLocalIdDeviceToGlobalIdDevice(eoj.getEchoObjectCode(), gatewayInformation.getFarmId()));
                            int inetAddressInt = Helper.convertLoraIdToInt(eoj.getNode().getAddress());
                            jsonObject.put("deviceId", Helper.convertLocalIdDeviceToGlobalIdDevice(inetAddressInt, gatewayInformation.getFarmId()));
                            if(Helper.isPump(eoj)) jsonObject.put("typeDevice",Helper.TypeDevice.PUMP.ordinal());
                            else if(Helper.isAgricultureSensor(eoj)) jsonObject.put("typeDevice",Helper.TypeDevice.AGRICULTURE_SENSOR.ordinal());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        String topic = "/iot_agriculture/status/device/online";
                        mqttConnector.publishMessage(jsonObject.toJSONString(),topic);
                        mqttConnector.disconnect();
                    }
                }catch (java.lang.NullPointerException e){
                    System.out.println("devicesList null");
                }

            }
        });
        stateDevicesBeforeTurnOffAll = devicesList.getDevices();
        while (true) {
            try {
                ArrayList<EchoNode> nodesOffline;
                //--------------------------------------
                devicesList.turnOffAll();
                System.out.println("Packet = "+NodeProfile.informG().reqInformInstanceListNotification().send().toString());
//                NodeProfile.informG().reqInformInstanceListNotification().send();
                Thread.sleep(timeToWaitUpdateDevices);
                //--------------------------------------
                nodesOffline = devicesList.getNodesOffline();
                MQTTConnector mqttConnector = new MQTTConnector();
                mqttConnector.connect();
                JSONObject jsonObject = new JSONObject();
                ArrayList<Long> newDevicesOffline = new ArrayList<Long>();
                for(EchoNode echoNode: nodesOffline){
                    if(stateDevicesBeforeTurnOffAll.get(echoNode)){
                        for(DeviceObject deviceObject : echoNode.getDevices()) {
                            int inetAddressInt = Helper.convertLoraIdToInt(deviceObject.getNode().getAddress());
                            Long globalDeviceId = Helper.convertLocalIdDeviceToGlobalIdDevice(inetAddressInt,GatewayInformation.getInstance().getFarmId());
                            newDevicesOffline.add(globalDeviceId);
                        }
                    }
                }
                String topic = "/iot_agriculture/status/devices/offline";
                if(newDevicesOffline.size()>0) {
                    mqttConnector.publishMessage(JSONValue.toJSONString(newDevicesOffline), topic);
                }
                mqttConnector.disconnect();

                stateDevicesBeforeTurnOffAll = devicesList.getDevices();
                System.out.println("DeviceManager 117");
                System.out.println(LocalDateTime.now());
                System.out.println(devicesList.toString());
                Thread.sleep(timeToReUpdateDevices);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
