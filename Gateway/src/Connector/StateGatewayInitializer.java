package Connector;

public class StateGatewayInitializer implements Runnable{
    MQTTConnector mqttConnector;
    StateGatewayInitializer(){
        mqttConnector = new MQTTConnector();
    }
    @Override
    public void run() {

    }
}
