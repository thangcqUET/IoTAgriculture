package model;

import java.sql.Timestamp;

public class Sensing {
    private long sensingID;
    private int deviceID;
    private int plotID;
    private int soilMoisture;
    private int humidity;
    private int lightLevel;
    private int temperature;
    private Timestamp timeOfMeasurement;

    public Sensing(long sensingID, int deviceID, int plotID, int soilMoisture, int humidity, int lightLevel, int temperature, Timestamp timeOfMeasurement) {
        this.sensingID = sensingID;
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.soilMoisture = soilMoisture;
        this.humidity = humidity;
        this.lightLevel = lightLevel;
        this.temperature = temperature;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Sensing(int deviceID, int plotID, int soilMoisture, int humidity, int lightLevel, int temperature, Timestamp timeOfMeasurement) {
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.soilMoisture = soilMoisture;
        this.humidity = humidity;
        this.lightLevel = lightLevel;
        this.temperature = temperature;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Sensing() {
    }

    @Override
    public String toString() {
        return "deviceID: "+deviceID+", plotID: "+plotID+", soilMoisture: "+soilMoisture+", humidity: "+humidity+", lightLevel: "+lightLevel+", temperature: "+temperature+", timestamp: "+timeOfMeasurement+"\n";
    }

    public long getSensingID() {
        return sensingID;
    }

    public void setSensingID(long sensingID) {
        this.sensingID = sensingID;
    }

    public int getDiviceID() {
        return deviceID;
    }

    public void setDiviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getPlotID() {
        return plotID;
    }

    public void setPlotID(int plotID) {
        this.plotID = plotID;
    }

    public int getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(int soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Timestamp getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(Timestamp timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }
}
