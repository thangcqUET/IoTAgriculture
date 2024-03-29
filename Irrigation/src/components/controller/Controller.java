package components.controller;

import components.OnControlListener;

public abstract class Controller {
    private Long deviceId;
    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Controller(Long deviceId) {
        this.deviceId = deviceId;
    }

    protected OnControlListener onControlListener;
    public void setOnControlListener(OnControlListener onControlListener){
        this.onControlListener = onControlListener;
    }
    protected void setStatus(Boolean status){
        onControlListener.onReceivedControllingData(status);
    }
    public void start(){}
}
