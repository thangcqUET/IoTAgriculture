package dataStructure;

import Connector.MQTTConnector;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import org.json.simple.JSONObject;
import utilites.Flags;
import utilites.Helper;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MqttDataPacketSender {
    class Packet{
        short tid;
        byte receivedStatus;
        JSONObject jsonObject;

        public Packet(Short tid, EchoObject eoj){
            this.tid = tid;
            this.receivedStatus = 0b0000;
            jsonObject = new JSONObject();
            try {
                jsonObject.put("deviceId",
                        Helper.convertLocalIdDeviceToGlobalIdDevice(
                                ByteBuffer.wrap(eoj.getNode().getAddress().getAddress()).getInt(),
                                GatewayInformation.getInstance().getFarmId()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        public boolean tick(byte flag, EchoProperty echoProperty){//true: received, false: haven't received
            receivedStatus|=flag;
            String dataName = null;
            Object data = null;
            switch (flag){
                case Flags.AIR_HUMIDITY_IS_RECEIVED:{
                    dataName = "airHumidity";
                    data = Helper.ConvertByteToFloat(echoProperty.edt);
                    break;
                }
                case Flags.AIR_TEMPERATURE_IS_RECEIVED:{
                    dataName = "airTemperature";
                    data = Helper.ConvertByteToFloat(echoProperty.edt);
                    break;
                }
//                case Flags.SOIL_TEMPERATURE_IS_RECEIVED:{
//                    dataName = "soilTemperature";
//                    data = Helper.ConvertByteToFloat(echoProperty.edt);
//                    break;
//                }
                case Flags.LIGHT_LEVEL_IS_RECEIVED:{
                    data = Helper.ConvertByteToInt(echoProperty.edt);
                    dataName = "lightLevel";
                    break;
                }
                case Flags.SOIL_MOISTURE_IS_RECEIVED:{
                    data = Helper.ConvertByteToFloat(echoProperty.edt);
                    dataName = "soilMoisture";
                    break;
                }
                default:{
                    dataName = "noName";
                    data = 0;
                }
            }
            jsonObject.put(dataName,data);
            if(receivedStatus == Flags.ALL_ARE_RECEIVED){
                MQTTConnector mqttConnector = new MQTTConnector();
                mqttConnector.connect();
                receivedStatus=0b0000;
                mqttConnector.publishMessage(jsonObject.toJSONString(),"/iot_agriculture/monitoring");
                mqttConnector.disconnect();
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "tid: "+tid+", receivedStatus: "+receivedStatus;
        }
    }
    private List<Packet> packetList;
    public MqttDataPacketSender(){
        packetList = new ArrayList<Packet>();
    }
    public void add(EchoObject eoj, short tid, byte flag, EchoProperty echoProperty){
        boolean isFound=false;

        for(Packet packet:packetList){
            if(tid == packet.tid){
                isFound = true;
                if(packet.tick(flag,echoProperty)){
                    packetList.remove(packet);
                }
                break;
            }
        }
        if(!isFound){
            Packet newPacket = new Packet(tid, eoj);
            newPacket.tick(flag, echoProperty);
            packetList.add(newPacket);
        }
    }

    @Override
    public String toString() {
        String ret = "List Packets:\n";
        for (Packet packet:packetList){
            ret+=packet.toString()+"\n";
        }
        ret+="--------------------\n";
        return ret;
    }
}
