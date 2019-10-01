package model;

import java.sql.Timestamp;

public class Sensing {
    private Long sensingID;
    private Long deviceID;
    private Integer plotID;
    private Float soilMoisture;
    private Float soilTemperature;
    private Float humidity;
    private Float temperature;
    private Integer lightLevel;
    
    private Timestamp timeOfMeasurement;

    public Sensing(Long sensingID, Long deviceID, Integer plotID, Float soilMoisture, Float soilTemperature, Float humidity, Float temperature, Integer lightLevel, Timestamp timeOfMeasurement) {
        this.sensingID = sensingID;
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.soilMoisture = soilMoisture;
        this.soilTemperature = soilTemperature;
        this.humidity = humidity;
        this.temperature = temperature;
        this.lightLevel = lightLevel;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Sensing(Long deviceID, Integer plotID, Float soilMoisture, Float soilTemperature, Float humidity, Float temperature, Integer lightLevel, Timestamp timeOfMeasurement) {
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.soilMoisture = soilMoisture;
        this.soilTemperature = soilTemperature;
        this.humidity = humidity;
        this.temperature = temperature;
        this.lightLevel = lightLevel;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Sensing() {
    }

    @Override
    public String toString() {
        return "sensingId: "+sensingID+", deviceID: "+deviceID+", plotID: "+plotID+", soilMoisture: "+soilMoisture+", soilTemperature"+soilTemperature+", humidity: "+humidity+", temperature: "+temperature+", lightLevel: "+lightLevel+", timestamp: "+timeOfMeasurement+"\n";
    }

    public Long getSensingID() {
        return sensingID;
    }

    public void setSensingID(Long sensingID) {
        this.sensingID = sensingID;
    }

    public Long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Long deviceID) {
        this.deviceID = deviceID;
    }

    public Integer getPlotID() {
        return plotID;
    }

    public void setPlotID(Integer plotID) {
        this.plotID = plotID;
    }

    public Float getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Float soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public Float getSoilTemperature() {
        return soilTemperature;
    }

    public void setSoilTemperature(Float soilTemperature) {
        this.soilTemperature = soilTemperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(Integer lightLevel) {
        this.lightLevel = lightLevel;
    }

    public Timestamp getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(Timestamp timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }
}
