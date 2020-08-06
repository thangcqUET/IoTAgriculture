package utilites;

import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.eoj.device.sensor.AgricultureSensor;
import dataStructure.GatewayInformation;
import main.TestAgricultureSensor;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Helper {
    public enum TypeDevice{
        NO_DEVICE, AGRICULTURE_SENSOR, PUMP
    }
    public static float ConvertByteToFloat(byte[] b) {
        ByteBuffer bb = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        return bb.getFloat();
    }
    public static byte[] floatToByteArray(float value) {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] {
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }
    public static int ConvertByteToInt(byte[] b)
    {
        int value= 0;
        for(int i=0;i<b.length;i++){
            int n=(b[i]<0?(int)b[i]+256:(int)b[i])<<(8*i);
            value+=n;
        }
        return value;
    }
    public static JSONObject mqttMessageToJsonObject(MqttMessage mqttMessage){
        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(mqttMessage.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static boolean deleteFile(String pathname){
        try {
            File file = new File(pathname);
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Long convertLocalIdDeviceToGlobalIdDevice(Integer loraId, Integer FarmId){
        return ((loraId|0x0L)<<32) | FarmId;
    }

    public static Integer convertGlobalIdDeviceToLocalIdDevice(Long globalIdDevice){
        return Long.valueOf(globalIdDevice>>32).intValue();
    }

    public static Integer convertLoraIdToInt(InetAddress loraId){
        return ByteBuffer.wrap(loraId.getAddress()).getInt();
    }
    public static void printShorts(Short[] a, int length){
        System.out.println("Short array:");
        for(int i = 0;i<length;i++){
            System.out.print(a[i]+" ");
        }
        System.out.println();
    }
    public static void printBytes(byte[] a, int length){
        System.out.println("Byte array:");
        for(int i = 0;i<length;i++){
            System.out.print(a[i]+" ");
        }
        System.out.println();
    }
    public static Integer convertDeviceIdToFarmId(Long deviceId){
        return (int)(deviceId&0x00000000FFFFFFFF);
    }

    public static boolean isPump(EchoObject echoObject){
        short echoClassCode = echoObject.getEchoClassCode();
        if(echoClassCode == Switch.ECHO_CLASS_CODE) return true;
        return false;
    }

    public static boolean isAgricultureSensor(EchoObject echoObject){
        short echoClassCode = echoObject.getEchoClassCode();
        if(echoClassCode == AgricultureSensor.ECHO_CLASS_CODE) return true;
        return false;
    }

    public static void restartApplication() throws IOException, URISyntaxException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(TestAgricultureSensor.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        /* is it a jar file? */
        if(!currentJar.getName().endsWith(".jar"))
            return;

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
}
