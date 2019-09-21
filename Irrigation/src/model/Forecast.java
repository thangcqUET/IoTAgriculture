package model;

import java.sql.Timestamp;

public class Forecast {
    private long forecastID;
    private int deviceID;
    private int plotID;
    private float precipitation;
    private int temperature;
    private int forecastStatus;
    private Timestamp timeOfMeasurement;

    public Forecast() {
    }

    public Forecast(long forecastID, int deviceID, int plotID, float precipitation, int temperature, int forecastStatus, Timestamp timeOfMeasurement) {
        this.forecastID = forecastID;
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.precipitation = precipitation;
        this.temperature = temperature;
        this.forecastStatus = forecastStatus;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Forecast(int deviceID, int plotID, float precipitation, int temperature, int forecastStatus, Timestamp timeOfMeasurement) {
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.precipitation = precipitation;
        this.temperature = temperature;
        this.forecastStatus = forecastStatus;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public long getForecastID() {
        return forecastID;
    }

    public void setForecastID(long forecastID) {
        this.forecastID = forecastID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getPlotID() {
        return plotID;
    }

    public void setPlotID(int plotID) {
        this.plotID = plotID;
    }

    public float getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(float precipitation) {
        this.precipitation = precipitation;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getForecastStatus() {
        return forecastStatus;
    }

    public void setForecastStatus(int forecastStatus) {
        this.forecastStatus = forecastStatus;
    }

    public Timestamp getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(Timestamp timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }
}
