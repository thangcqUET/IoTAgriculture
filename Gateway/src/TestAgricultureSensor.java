import java.io.IOException;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.sensor.AgricultureSensor;
import com.sonycsl.echo.eoj.device.sensor.HumiditySensor;
import com.sonycsl.echo.eoj.device.sensor.TemperatureSensor;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;
import com.sonycsl.echo.processing.defaults.DefaultController;
public class TestAgricultureSensor {
    public static void main(String[] args) {
        setup();
    }
    public static void setup() {
        Echo.addEventListener(new Echo.EventListener(){
            @Override
            public void onNewAgricultureSensor(AgricultureSensor device) {
                super.onNewAgricultureSensor(device);
                System.out.println("Agriculture sensor");
            }

            @Override
            public void onNewTemperatureSensor(TemperatureSensor device) {
                super.onNewTemperatureSensor(device);
                System.out.println("Temperature sensor");
            }

            @Override
            public void onNewHumiditySensor(HumiditySensor device) {
                super.onNewHumiditySensor(device);
                System.out.println("Humidity sensor");
            }
        });
        try {
            Echo.start(new DefaultNodeProfile(), new DeviceObject[]{new DefaultController()});
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
// Query existing node profiles (amounts to finding existing nodes)
                NodeProfile.informG().reqInformInstanceListNotification().send();
                EchoNode[] nodes = Echo.getNodes();
                for (int i = 0; i < nodes.length; ++i) {
                    EchoNode en = nodes[i];
                    System.out.println("node id = " + en.getAddress().getHostAddress());
                    System.out.println("node profile = " + en.getNodeProfile());
                    DeviceObject[] dos = en.getDevices();
                    System.out.println("There are " + dos.length + " devices in this node");
                    for (int j = 0; j < dos.length; ++j) {
                        DeviceObject d = dos[j];
                        System.out.println("device type = " + d.getClass().getSuperclass().getSimpleName());
                        System.out.println(d.getClassCode());
                    }
                    System.out.println("----");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Wait 10 seconds.
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
