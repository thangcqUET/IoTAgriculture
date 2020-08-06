package processor;

import Connector.MQTTConnector;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.node.EchoNode;
//import com.sun.tools.classfile.ConstantPool;
import dataStructure.DevicesList;
import dataStructure.GatewayInformation;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import utilites.Helper;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ControlReceiver{
    private static ControlReceiver instance=null;
    private HashMap<Integer,Thread> currentThreads=null;
    private ControlReceiver(){
        currentThreads = new HashMap<Integer, Thread>();
    }

    public static synchronized ControlReceiver getInstance() {
        if(instance==null){
            instance=new ControlReceiver();
        }
        return instance;
    }

    public void sendToDevice(MqttMessage mqttMessage) {
        JSONObject jsonObject = Helper.mqttMessageToJsonObject(mqttMessage);
        Long deviceId;
        Float timeToWater;
        if(jsonObject.get("deviceId") instanceof Long) {
            deviceId = ((Long) jsonObject.get("deviceId"));
            System.out.println("device: " + deviceId);
        }else{
            return;
        }
        if(jsonObject.get("timeToWater") instanceof Double){
            timeToWater = (float) Math.ceil(((Double) jsonObject.get("timeToWater")));
            System.out.println("timeToWater: "+timeToWater);
        }else{
            return;
        }
        Integer idLocalDevice = Helper.convertGlobalIdDeviceToLocalIdDevice(deviceId);

        //turn off reliable transmitter
        ArrayList<EchoNode> onlineNodes = DevicesList.getInstance().getNodesOnline();
        System.out.println(DevicesList.getInstance());
        DeviceObject currentDeviceObject=null;
        for(EchoNode echoNode : onlineNodes){
            for(DeviceObject deviceObj : echoNode.getDevices()){
                if(Helper.convertLoraIdToInt(deviceObj.getNode().getAddress()) == idLocalDevice){
                    currentDeviceObject = deviceObj;
                }
            }
            if(currentDeviceObject!=null) break;
        }
        if(currentDeviceObject instanceof Switch) {
            Switch switchDevice = (Switch) currentDeviceObject;
            try {
                int TID = switchDevice.set().reqSetTurnOnDuration(Helper.floatToByteArray(timeToWater)).send().getTID();
                System.out.println("send packet control TID: "+TID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //
//        System.out.println(Helper.convertLocalIdDeviceToGlobalIdDevice(idLocalDevice,1)+" "+idLocalDevice);

        /** //turn off reliable transmitter
        ReliableTransmitter newTransmitter = new ReliableTransmitter(idLocalDevice,timeToWater);
        Thread newThread = new Thread(newTransmitter);
        if(!currentThreads.containsKey(idLocalDevice)){
            currentThreads.put(idLocalDevice,newThread);
            printCurrentThreads();
        }else{
            currentThreads.get(idLocalDevice).interrupt();
            currentThreads.replace(idLocalDevice,newThread);
            printCurrentThreads();
        }
        newThread.start();
         **/
    }

    void printCurrentThreads(){
        System.out.println("--ReliableTransmitter threads--");
        currentThreads.forEach((key,val)-> System.out.println(key+": "+val.getId()));
        System.out.println("--end--");
    }
}
