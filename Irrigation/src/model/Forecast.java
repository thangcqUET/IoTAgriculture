package model;

import java.sql.Timestamp;

public class Forecast {
    private Long forecastID;
    private Long deviceID;
    private Integer plotID;
    private Float precipitation;
    private Float humidity;
    private Float temperature;
    private String forecastStatus;
    private Timestamp timeOfMeasurement;

    public Forecast() {
    }

    public Forecast(Long forecastID, Long deviceID, Integer plotID, Float precipitation, Float humidity, Float temperature, String forecastStatus, Timestamp timeOfMeasurement) {
        this.forecastID = forecastID;
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.temperature = temperature;
        this.forecastStatus = forecastStatus;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Forecast(Long deviceID, Integer plotID, Float precipitation, Float humidity, Float temperature, String forecastStatus, Timestamp timeOfMeasurement) {
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.temperature = temperature;
        this.forecastStatus = forecastStatus;
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public Long getForecastID() {
        return forecastID;
    }

    public void setForecastID(Long forecastID) {
        this.forecastID = forecastID;
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

    public Float getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Float precipitation) {
        this.precipitation = precipitation;
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

    public String getForecastStatus() {
        return forecastStatus;
    }

    public void setForecastStatus(String forecastStatus) {
        this.forecastStatus = forecastStatus;
    }

    public Timestamp getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(Timestamp timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }
}
