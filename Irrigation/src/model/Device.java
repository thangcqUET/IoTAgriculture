package model;

public class Device {
    private Long deviceID;
    private Integer deviceTypeID;
    private String deviceName;
    private Integer plotID;

    public Device(Long deviceID, Integer deviceTypeID, String deviceName, Integer plotID) {
        this.deviceID = deviceID;
        this.deviceTypeID = deviceTypeID;
        this.deviceName = deviceName;
        this.plotID = plotID;
    }

    @Override
    public String toString() {
        return "deviceID: "+deviceID+", deviceTypeID: "+deviceTypeID+", deviceName: "+deviceName+", plotID: "+plotID+'\n';
    }

    public Long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Long deviceID) {
        this.deviceID = deviceID;
    }

    public Integer getDeviceTypeID() {
        return deviceTypeID;
    }

    public void setDeviceTypeID(Integer deviceTypeID) {
        this.deviceTypeID = deviceTypeID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getPlotID() {
        return plotID;
    }

    public void setPlotID(Integer plotID) {
        this.plotID = plotID;
    }
}
