package model;

public class DeviceType {
    private Integer deviceTypeId;
    private String deviceType;

    public DeviceType(Integer deviceTypeId, String deviceType) {
        this.deviceTypeId = deviceTypeId;
        this.deviceType = deviceType;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
