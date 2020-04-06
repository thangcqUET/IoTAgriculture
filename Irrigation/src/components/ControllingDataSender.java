package components;

import Connector.MQTTConnector;
import Utilities.Helper;
import components.autoController.MPC;
import components.controller.Controller;
import components.controller.CycleController;
import components.controller.LoopController;
import org.json.simple.JSONObject;

public class ControllingDataSender {
    //private Controller controller;
    public ControllingDataSender() {
        MPC mpc = new MPC();
        mpc.process();
//        controller = new LoopController(1685559915315201L);
//        final MQTTConnector mqttConnector;
//        mqttConnector = new MQTTConnector();
//        mqttConnector.connect();
//        final String topic = "/iot_agriculture/controlling/"+ Helper.convertDeviceIdToFarmId(controller.getDeviceId());
//        controller.setOnControlListener(new OnControlListener() {
//            @Override
//            public void onReceivedControllingData(Boolean status) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("status",status);
//                jsonObject.put("deviceId",controller.getDeviceId());
//                mqttConnector.publishMessage(jsonObject.toJSONString(),topic);
//            }
//
//            @Override
//            public void onReceivedControllingData(Float amountOfWater) {
//                super.onReceivedControllingData(amountOfWater);
//                //gui data
//            }
//        });
//        controller.start();
    }
}
