package components.autoController;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class DeviceControlUnitManager {
    private static DeviceControlUnitManager instance;
    private static Queue<DeviceControlUnit> deviceControlUnitQueue;
    private static HashMap<Long,Boolean> autoControllingDevices;
    private static HashMap<Long,Boolean> onDevices;
    private DeviceControlUnitManager(){
        deviceControlUnitQueue = new PriorityQueue<DeviceControlUnit>();
        autoControllingDevices = new HashMap<Long,Boolean>();
        onDevices = new HashMap<Long,Boolean>();
    }
    public static synchronized DeviceControlUnitManager getInstance(){
        if(instance==null){
            instance = new DeviceControlUnitManager();
        }
        return instance;
    }
    public Boolean isEmpty(){
        return deviceControlUnitQueue.isEmpty();
    }
    public synchronized void add(DeviceControlUnit dcu){
        deviceControlUnitQueue.add(dcu);
        autoControllingDevices.put(dcu.getDeviceId(),true);
        onDevices.put(dcu.getDeviceId(),true);
    }
    public synchronized DeviceControlUnit peek(){
        return deviceControlUnitQueue.peek();
    }
    public synchronized DeviceControlUnit element(){
        return deviceControlUnitQueue.element();
    }
    public synchronized DeviceControlUnit remove(){
        DeviceControlUnit ret = deviceControlUnitQueue.peek();
        if(autoControllingDevices.get(ret.getDeviceId())!=ret.getAuto())
            ret.setAuto(autoControllingDevices.get(ret.getDeviceId()));
        if(onDevices.get(ret.getDeviceId())!=ret.getOn())
            ret.setOn(onDevices.get(ret.getDeviceId()));

        autoControllingDevices.remove(deviceControlUnitQueue.peek().getDeviceId());
        onDevices.remove(deviceControlUnitQueue.peek().getDeviceId());
        DeviceControlUnit dcu = deviceControlUnitQueue.remove();
        return dcu;
    }
    public synchronized Boolean setAutoControl(Long deviceId, Boolean isAuto){
        return autoControllingDevices.replace(deviceId,isAuto);
    }
    public Boolean isAutoControl(Long deviceId){
        return autoControllingDevices.get(deviceId);
    }

    public synchronized Boolean setOnControl(Long deviceId, Boolean isOn){
        return onDevices.replace(deviceId,isOn);
    }
    public Boolean isOnControl(Long deviceId){
        System.out.println(onDevices);
        return onDevices.get(deviceId);
    }

    public Boolean isExist(Long deviceId){
        return onDevices.get(deviceId)==null?false:true;
    }
    @Override
    public String toString() {
        return deviceControlUnitQueue.toString();
    }
}
