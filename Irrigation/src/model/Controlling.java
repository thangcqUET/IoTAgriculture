package model;

import java.sql.Timestamp;

public class Controlling {
    private Long controllingID;
    private Long deviceID;
    private Integer plotID;
    private Float amountOfWater;
    private Integer wateringDuration;
    private Timestamp timeOfControl;


    public Controlling(Long controllingID, Long deviceID, Integer plotID, Float amountOfWater, Integer wateringDuration, Timestamp timeOfControl) {
        this.controllingID = controllingID;
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.amountOfWater = amountOfWater;
        this.wateringDuration = wateringDuration;
        this.timeOfControl = timeOfControl;
    }

    public Controlling(Long deviceID, Integer plotID, Float amountOfWater, Integer wateringDuration, Timestamp timeOfControl) {
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.amountOfWater = amountOfWater;
        this.wateringDuration = wateringDuration;
        this.timeOfControl = timeOfControl;
    }

    public Long getControllingID() {
        return controllingID;
    }

    public void setControllingID(Long controllingID) {
        this.controllingID = controllingID;
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

    public Float getAmountOfWater() {
        return amountOfWater;
    }

    public void setAmountOfWater(Float amountOfWater) {
        this.amountOfWater = amountOfWater;
    }

    public Integer getWateringDuration() {
        return wateringDuration;
    }

    public void setWateringDuration(Integer wateringDuration) {
        this.wateringDuration = wateringDuration;
    }

    public Timestamp getTimeOfControl() {
        return timeOfControl;
    }

    public void setTimeOfControl(Timestamp timeOfControl) {
        this.timeOfControl = timeOfControl;
    }
}
