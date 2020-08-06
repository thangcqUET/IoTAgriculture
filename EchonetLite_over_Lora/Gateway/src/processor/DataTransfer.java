package processor;

import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.eoj.device.sensor.AgricultureSensor;
import com.sonycsl.echo.node.EchoNode;
import dataStructure.DevicesList;
import dataStructure.MqttDataPacketSender;
import dataStructure.MqttDataPacketSender;
import org.json.simple.JSONObject;
import utilites.Flags;
import utilites.Helper;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class DataTransfer implements Runnable {
    private int timeToUpdateData;
    public DataTransfer(){
        timeToUpdateData = 120000;
    }

    public DataTransfer(int timeToUpdateData) {
        this.timeToUpdateData = timeToUpdateData;
    }

    @Override
    public void run() {
        Echo.addEventListener(new Echo.EventListener(){
            @Override
            public void onNewAgricultureSensor(AgricultureSensor device) {
                super.onNewAgricultureSensor(device);
                System.out.println("New agriculture sensor");
                device.setReceiver(new AgricultureSensor.Receiver(){
                    MqttDataPacketSender mqttDataPacketSender = new MqttDataPacketSender();
                    @Override
                    protected void onGetAirTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetAirTemperature(eoj, tid, esv, property, success);
                        mqttDataPacketSender.add(eoj, tid,Flags.AIR_TEMPERATURE_IS_RECEIVED,property);
                    }

                    @Override
                    protected void onGetAirHumidity(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetAirHumidity(eoj, tid, esv, property, success);
                        mqttDataPacketSender.add(eoj, tid,Flags.AIR_HUMIDITY_IS_RECEIVED,property);
                    }

//                    @Override
//                    protected void onGetSoilTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
//                        super.onGetSoilTemperature(eoj, tid, esv, property, success);
//                        mqttDataPacketSender.add(eoj, tid,Flags.SOIL_TEMPERATURE_IS_RECEIVED,property);
//                    }

                    @Override
                    protected void onGetSoilMoisture(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetSoilMoisture(eoj, tid, esv, property, success);
                        mqttDataPacketSender.add(eoj, tid,Flags.SOIL_MOISTURE_IS_RECEIVED,property);
                    }

                    @Override
                    protected void onGetLightLevel(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetLightLevel(eoj, tid, esv, property, success);
                        mqttDataPacketSender.add(eoj, tid,Flags.LIGHT_LEVEL_IS_RECEIVED,property);
                    }
                });
            }

            @Override
            public void onNewSwitch(Switch device) {
                super.onNewSwitch(device);
                System.out.println("New Switch");
            }
        });
        while(true){
            System.out.println("Get data...");
            DevicesList devicesList = DevicesList.getInstance();
            ArrayList<EchoNode> echoNodes = devicesList.getNodesOnline();
            System.out.println("Data transfer line 82");
            System.out.println("Length Echonodes: "+echoNodes.size());
            for(EchoNode echoNode : echoNodes){
                DeviceObject[] deviceObjects= echoNode.getDevices();
                System.out.println("Data transfer line 86");
                System.out.println("Length device object: "+deviceObjects.length+" belong Echonode: "+ echoNode);
                for(DeviceObject deviceObject : deviceObjects){
                    if(deviceObject instanceof AgricultureSensor) {
                        try {
                            System.out.println("Device: "+ deviceObject);
                            ((AgricultureSensor)deviceObject).get()
                                    .reqGetMeasuredAirTemperatureValue()
                                    .reqGetMeasuredAirHumidityValue()
                                    .reqGetMeasuredLightLevelValue()
//                                    .reqGetMeasuredSoilTemperatureValue()
                                    .reqGetMeasuredSoilMoistureValue()
                                    .send();
                            Thread.sleep(500);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                Thread.sleep(timeToUpdateData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
