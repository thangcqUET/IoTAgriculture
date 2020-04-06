package test.components.controller;

import test.components.OnControlListener;

public abstract class Controller {
    Boolean status;
    Long deviceId;
    Float amountOfWater;
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Controller(Long deviceId) {
        this.deviceId = deviceId;
    }

    private OnControlListener onControlListener;
    public void setOnControlListener(OnControlListener onControlListener){
        this.onControlListener = onControlListener;
    }
    protected void setStatus(Boolean status){
        this.status = status;
        onControlListener.onReceivedControllingData(status);
    }
    protected void setAmountOfWater(Float amountOfWater){
        this.amountOfWater=amountOfWater;
        onControlListener.onReceivedControllingData(amountOfWater);
    }
    public abstract void start();
}
