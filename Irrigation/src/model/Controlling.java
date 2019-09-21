package model;

import java.sql.Timestamp;

public class Controlling {
    private long controllingID;
    private int deviceID;
    private int plotID;
    private float amountOfWater;
    private int wateringDuration;
    private Timestamp timeOfControl;


    public Controlling(long controllingID, int deviceID, int plotID, float amountOfWater, int wateringDuration, Timestamp timeOfControl) {
        this.controllingID = controllingID;
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.amountOfWater = amountOfWater;
        this.wateringDuration = wateringDuration;
        this.timeOfControl = timeOfControl;
    }

    public Controlling(int deviceID, int plotID, float amountOfWater, int wateringDuration, Timestamp timeOfControl) {
        this.deviceID = deviceID;
        this.plotID = plotID;
        this.amountOfWater = amountOfWater;
        this.wateringDuration = wateringDuration;
        this.timeOfControl = timeOfControl;
    }

    public long getControllingID() {
        return controllingID;
    }

    public void setControllingID(long controllingID) {
        this.controllingID = controllingID;
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

    public float getAmountOfWater() {
        return amountOfWater;
    }

    public void setAmountOfWater(float amountOfWater) {
        this.amountOfWater = amountOfWater;
    }

    public int getWateringDuration() {
        return wateringDuration;
    }

    public void setWateringDuration(int wateringDuration) {
        this.wateringDuration = wateringDuration;
    }

    public Timestamp getTimeOfControl() {
        return timeOfControl;
    }

    public void setTimeOfControl(Timestamp timeOfControl) {
        this.timeOfControl = timeOfControl;
    }
}
