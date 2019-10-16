package Connector;

import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.sensor.AgricultureSensor;
import com.sonycsl.echo.eoj.device.sensor.HumiditySensor;
import com.sonycsl.echo.eoj.device.sensor.TemperatureSensor;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.processing.defaults.DefaultController;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static com.sonycsl.echo.Echo.getNodes;

public class EchonetController {
    private ArrayList<DeviceObject> mDevices;


    public EchonetController() {
        mDevices = new ArrayList<DeviceObject>();
        Echo.addEventListener(new Echo.EventListener(){
            @Override
            public void onFoundNode(EchoNode node) {
                super.onFoundNode(node);
                System.out.println("Found node "+node.getNodeProfile());
            }
        });
        Echo.addEventListener(new Echo.EventListener(){
            @Override
            public void onNewAgricultureSensor(AgricultureSensor device) {
                super.onNewAgricultureSensor(device);
                System.out.println("New Agriculture Sensor");
                device.setReceiver(new AgricultureSensor.Receiver(){
                    @Override
                    protected void onGetAirHumidity(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetAirHumidity(eoj, tid, esv, property, success);
                        System.out.println("eoj: "+eoj+" esv: "+esv+" property: "+property);
                    }

                    @Override
                    protected void onGetAirTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetAirTemperature(eoj, tid, esv, property, success);
                        System.out.println("eoj: "+eoj+" esv: "+esv+" property: "+property);
                    }

                    @Override
                    protected void onGetSoilMoisture(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetSoilMoisture(eoj, tid, esv, property, success);
                        System.out.println("eoj: "+eoj+" esv: "+esv+" property: "+property);
                    }

                    @Override
                    protected void onGetSoilTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetSoilTemperature(eoj, tid, esv, property, success);
                        System.out.println("eoj: "+eoj+" esv: "+esv+" property: "+property);
                    }

                    @Override
                    protected void onGetLightLevel(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        super.onGetLightLevel(eoj, tid, esv, property, success);
                        System.out.println("eoj: "+eoj+" esv: "+esv+" property: "+property);
                    }
                });
                try {
                    System.out.println("Send req get:");
                    device.get().reqGetMeasuredAirHumidityValue()
                            .reqGetMeasuredAirTemperatureValue()
                            .reqGetMeasuredSoilMoistureValue()
                            .reqGetMeasuredSoilTemperatureValue()
                            .send();
                    System.out.println("Sent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Echo.start(new DefaultNodeProfile(), new DeviceObject[]{new DefaultController()});
            NodeProfile.informG().reqInformInstanceListNotification().send();

            Thread.sleep(10000);
            EchoNode[] echoNodes = Echo.getNodes();
            for(EchoNode e: echoNodes){
                System.out.println(e.getNodeProfile());
                DeviceObject[] deviceObjects = e.getDevices();
                for(DeviceObject deviceObject: deviceObjects){
                    System.out.println(""+deviceObject.getEchoClassCode()+", "+deviceObject.getInstanceCode());
                }
                System.out.println("---------------");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static float ConvertByteToFloat(byte[] b) {
        ByteBuffer bb = ByteBuffer.wrap(b);
        return bb.getFloat();
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
    int bti(byte[] b){
        int ret = 0 ;
        for (byte aB : b) ret = (ret << 8) | (aB & 0xff);
        return ret ;
    }

    // Convert type(short)0x0290 to 0x02(group code) and 0x90(class code) in byte
    // type short like above is used to present Group code and Class code
    //  in com.sonycsl.echo.eoj.device.housing.... library
    public static byte getClassGroupCode(short code ) {
        return (byte)((code >> 8) & 0xFF);
    }

    public static byte getClassCode(short code ) {
        return (byte)(code & 0xFF);
    }
}