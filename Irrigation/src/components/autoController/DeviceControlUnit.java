package components.autoController;

import Connector.MQTTConnector;
import DAO.DeviceDao;
import DAO.FarmDao;
import DAO.PlotDao;
import DAO.SensingDao;
import Utilities.Helper;
import components.OnControlListener;
import components.controller.Controller;
import model.Sensing;
import model.WeatherForecast;
import org.json.simple.JSONObject;

import java.time.*;

public class DeviceControlUnit extends Controller implements Comparable<DeviceControlUnit>{
    private String locateId;
    private Float upperThreshold;
    private Float lowerThreshold;
    private Integer currentSoilMoisture;
    private WeatherForecast weatherForecast;
    private LocalTime latestIrrigationTime;
    private Float amountOfWater;
    private Boolean isAuto;
    private Boolean isOn;
    private Float pumpSpeed;
    public DeviceControlUnit(){
        super(1685555620347905L);
        locateId = "353412"; //Hanoi
        latestIrrigationTime = LocalTime.now().withNano(0).withSecond(0).withMinute(1);// test, trong thực tế sẽ là giờ tròn, tức X h 00 m, minute = 1 vi de update WeatherForecast truoc
        pumpSpeed=0.75F; //ml/s
        upperThreshold=100F;
        lowerThreshold=10F;
        isAuto=true;
        isOn=true;
        SensingDao sensingDao = new SensingDao();
        DeviceDao deviceDao = new DeviceDao();
        Sensing sensing = sensingDao.getNewestSensingByPlotId(deviceDao.getPlotIdById(getDeviceId()));
        currentSoilMoisture=sensing.getSoilMoisture().intValue();
        System.out.println("DeviceControlUnit line 39");
        System.out.println("SoilMoisture: "+currentSoilMoisture);
        DeviceControlUnit currentDCU=this;
        final String topic = "/iot_agriculture/controlling/"+ Helper.convertDeviceIdToFarmId(currentDCU.getDeviceId());
        currentDCU.setOnControlListener(new OnControlListener() {
            @Override
            public void onReceivedControllingData(Float amountOfWater) {
                super.onReceivedControllingData(amountOfWater);
                final MQTTConnector mqttConnector;
                mqttConnector = new MQTTConnector();
                mqttConnector.connect();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("timeToWater",amountOfWater/pumpSpeed);
                jsonObject.put("deviceId",currentDCU.getDeviceId());
                mqttConnector.publishMessage(jsonObject.toJSONString(),topic);
                mqttConnector.disconnect();
            }
        });
    }
    public DeviceControlUnit(Long deviceId) {
        super(deviceId);
        locateId = "353412"; //Hanoi
        latestIrrigationTime = LocalTime.now().withNano(0).withSecond(0).withMinute(0);// test, trong thực tế sẽ là giờ tròn, tức X h 00 m
        pumpSpeed=0.75F; //ml/s
        upperThreshold=100F;
        lowerThreshold=10F;
        isAuto=true;
        isOn=true;
        SensingDao sensingDao = new SensingDao();
        DeviceDao deviceDao = new DeviceDao();
        Sensing sensing = sensingDao.getNewestSensingByPlotId(deviceDao.getPlotIdById(getDeviceId()));
        currentSoilMoisture=sensing.getSoilMoisture().intValue();
        System.out.println("DeviceControlUnit line 39");
        System.out.println("SoilMoisture: "+currentSoilMoisture);
        DeviceControlUnit currentDCU=this;
        final String topic = "/iot_agriculture/controlling/"+ Helper.convertDeviceIdToFarmId(currentDCU.getDeviceId());
        currentDCU.setOnControlListener(new OnControlListener() {
            @Override
            public void onReceivedControllingData(Float amountOfWater) {
                super.onReceivedControllingData(amountOfWater);
                final MQTTConnector mqttConnector;
                mqttConnector = new MQTTConnector();
                mqttConnector.connect();
                JSONObject jsonObject = new JSONObject();
                System.out.println("amountOfWater: "+amountOfWater);
                System.out.println("pumpSpeed: "+pumpSpeed);
                jsonObject.put("timeToWater",amountOfWater/pumpSpeed);
                jsonObject.put("deviceId",currentDCU.getDeviceId());
                mqttConnector.publishMessage(jsonObject.toJSONString(),topic);
                mqttConnector.disconnect();
            }
        });
    }

//    public void setLocateID(String locateID) {
//        this.locateID = locateID;
//    }

    public void setUpperThreshold(Float upperThreshold) {
        this.upperThreshold = upperThreshold;
    }

    public void setLowerThreshold(Float lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }

    public void setCurrentSoilMoisture(Integer currentSoilMoisture) {
        this.currentSoilMoisture = currentSoilMoisture;
    }

    public LocalTime getLatestIrrigationTime() {
        return latestIrrigationTime;
    }

    public Long getDeviceId() {
        return super.getDeviceId();
    }

    public String getLocateId() {
        return locateId;
    }

    public Float getUpperThreshold() {
        return upperThreshold;
    }

    public Float getLowerThreshold() {
        return lowerThreshold;
    }

    public Integer getCurrentSoilMoisture() {
        return currentSoilMoisture;
    }

    public WeatherForecast getWeatherForecast() {
        if(weatherForecast==null){
            weatherForecast=WeatherForecasts.getInstance().getWeatherForecastByLocateId(locateId);
//            System.out.println("DeviceControlUnit line 133");
//            System.out.println(weatherForecast);
        }else {
            weatherForecast.update();
        }
        return weatherForecast;
    }

    public void setAmountOfWater(Float amountOfWater){
        onControlListener.onReceivedControllingData(amountOfWater);
        latestIrrigationTime = latestIrrigationTime.plusHours(1);
    }

    public Float getPumpSpeed() {
        return pumpSpeed;
    }

    public void setPumpSpeed(Float pumpSpeed) {
        this.pumpSpeed = pumpSpeed;
    }

    public Boolean getAuto() {
        return isAuto;
    }

    public void setAuto(Boolean auto) {
        isAuto = auto;
    }

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }

    public boolean needProcess(){
        return isOn&&isAuto;
    }
    @Override
    public int compareTo(DeviceControlUnit o) {
        return latestIrrigationTime.compareTo(o.latestIrrigationTime);
    }

    @Override
    public String toString() {
        //return super.toString();
        return "\n------------" +
                "\ndevice Id: " +getDeviceId()+
                "\nupperThreshold: "+upperThreshold+
                "\nlowerThreshold: "+lowerThreshold+
                "\ncurrentSoilMoisture: "+currentSoilMoisture+
                "\nlatestIrrigationTime: "+latestIrrigationTime+
                "\npumpSpeed: "+pumpSpeed+
                "\n-----------\n";
    }
}
