package processor;

import Connector.MQTTConnector;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.node.EchoNode;
import dataStructure.DevicesList;
import dataStructure.GatewayInformation;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import utilites.Helper;


import java.io.FileNotFoundException;
import java.io.IOException;

public class ControlReceiver implements Runnable{
    private MqttMessage mqttMessage;
    public ControlReceiver(MqttMessage mqttMessage){
        this.mqttMessage = mqttMessage;
    }
    @Override
    public void run() {
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
            timeToWater = ((Double) jsonObject.get("timeToWater")).floatValue();
            System.out.println("timeToWater: "+timeToWater);
        }else{
            return;
        }
        Integer echoObjectCode = Helper.convertGlobalIdDeviceToLocalIdDevice(deviceId);
        System.out.println(Helper.convertLocalIdDeviceToGlobalIdDevice(echoObjectCode,1)+" "+echoObjectCode);
        ReliableTransmitter reliableTransmitter = new ReliableTransmitter();
        System.out.println("SET: "+reliableTransmitter.setC(echoObjectCode,timeToWater)+" "+timeToWater);
    }
}
