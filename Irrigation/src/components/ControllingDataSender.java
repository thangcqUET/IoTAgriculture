package components;

import Connector.MQTTConnector;
import Utilities.Helper;
import components.autoController.MPC;
import components.controller.Controller;
import components.controller.CycleController;
import components.controller.LoopController;
import org.json.simple.JSONObject;

public class ControllingDataSender {
    private Controller controller;
    public ControllingDataSender() {
//        MPC mpc = new MPC();
//        mpc.process();
        controller = new LoopController(73014444033L);
        final MQTTConnector mqttConnector;
        mqttConnector = new MQTTConnector();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mqttConnector.connect();
        final String topic = "/iot_agriculture/controlling/"+ Helper.convertDeviceIdToFarmId(controller.getDeviceId());
        controller.setOnControlListener(new OnControlListener() {
            @Override
            public void onReceivedControllingData(Boolean status) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status",status);
                jsonObject.put("deviceId",controller.getDeviceId());
                mqttConnector.publishMessage(jsonObject.toJSONString(),topic);
            }

            @Override
            public void onReceivedControllingData(Float amountOfWater) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("timeToWater",amountOfWater/0.75F);
                jsonObject.put("deviceId",controller.getDeviceId());
                mqttConnector.publishMessage(jsonObject.toJSONString(),topic);
            }
        });
        controller.start();
    }
}
