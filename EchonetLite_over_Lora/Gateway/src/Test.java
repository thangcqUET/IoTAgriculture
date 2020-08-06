import Connector.MQTTConnector;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Test {
    static Boolean isNewThread;
    static Thread currentThread=null;
    public static void main(String[] args) {
        MQTTConnector mqttConnectorSub = new MQTTConnector();
        mqttConnectorSub.connect();
        mqttConnectorSub.subcribe("/test/");
        mqttConnectorSub.getmMqttClient().setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                System.out.println("received message");
                run();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
    public static void run(){

        System.out.println("run");
//        if(currentThread!=null) currentThread.interrupt();
        currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                isNewThread = true;
                System.out.println(Thread.currentThread().getId()+" "+isNewThread);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isNewThread = false;
                System.out.println(Thread.currentThread().getId());
//                while (!Thread.interrupted()){
//                }
                System.out.println("Has new thread, bye...");
            }
        });
        System.out.println("start thread first "+currentThread);
        currentThread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
