package model;

public class Device {
    private int deviceID;
    private int deviceTypeID;
    private String deviceName;
    private int plotID;

    public Device(int deviceID, int deviceTypeID, String deviceName, int plotID) {
        this.deviceID = deviceID;
        this.deviceTypeID = deviceTypeID;
        this.deviceName = deviceName;
        this.plotID = plotID;
    }

    @Override
    public String toString() {
        return "deviceID: "+deviceID+", deviceTypeID: "+deviceTypeID+", deviceName: "+deviceName+", plotID: "+plotID+'\n';
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getDeviceTypeID() {
        return deviceTypeID;
    }

    public void setDeviceTypeID(int deviceTypeID) {
        this.deviceTypeID = deviceTypeID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getPlotID() {
        return plotID;
    }

    public void setPlotID(int plotID) {
        this.plotID = plotID;
    }
}
