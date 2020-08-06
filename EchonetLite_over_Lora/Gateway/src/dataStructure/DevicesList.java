package dataStructure;

import com.sonycsl.echo.Echo;
import com.sonycsl.echo.node.EchoNode;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class DevicesList {
    private static DevicesList instance ;
    private boolean isUpdating;
    private HashMap<InetAddress,Boolean> devices;
    private DevicesList(){
            devices = new HashMap<>();
            isUpdating = false;
    }

    public HashMap<EchoNode, Boolean> getDevices() {
        HashMap<EchoNode,Boolean> devicesCopy = new HashMap<>();
        devices.forEach((inetAddress, aBoolean) -> {
            devicesCopy.put(Echo.getNode(inetAddress.getHostAddress()),aBoolean);
        });
        return devicesCopy;
    }

    public static DevicesList getInstance(){
        if(instance==null){
            instance = new DevicesList();
        }
        return instance;
    }

    public synchronized boolean add(InetAddress inetAddress){
        isUpdating = true;

        if(this.devices.containsKey(inetAddress)) return false;
        devices.put(inetAddress,true);
        isUpdating = false;
        return true;
    }
    public synchronized boolean turnOn(InetAddress inetAddress){
        isUpdating = true;
        if(devices==null){
            isUpdating=false;
            return false;
        }
        if(devices.get(inetAddress)==true){
            isUpdating = false;
            return true;
        }
        if(devices.containsKey(inetAddress)) {
            devices.replace(inetAddress, false, true);
            isUpdating = false;
            return true;
        }
        isUpdating = false;
        return false;
    }
    public synchronized boolean turnOffAll(){
        isUpdating = true;
        if(devices==null){
            isUpdating = false;
            return false;
        }
        devices.forEach(((inetAddress, aBoolean) -> {
            devices.replace(inetAddress,true,false);
        }));
        isUpdating = false;
        return true;
    }

    // co the loi
    public synchronized void removeOfflineDevices(){
        isUpdating = true;
        devices.forEach(((inetAddress, aBoolean) -> {
            if(aBoolean==false){
                devices.remove(inetAddress);
            }
        }));
        isUpdating = false;
    }

    public ArrayList<EchoNode> getNodesOnline(){
        {
            if(isUpdating) System.out.println("DevicesList is updating...");
            while (isUpdating) {
            }
        }
        ArrayList<EchoNode> echoNodes = new ArrayList<>();
        devices.forEach(((inetAddress, aBoolean) -> {
            if(aBoolean==true){
                echoNodes.add(Echo.getNode(inetAddress.getHostAddress()));

            }
        }));
        return echoNodes;
    }

    public ArrayList<EchoNode> getNodesOffline(){
        {
            if(isUpdating) System.out.println("DevicesList is updating...");
            while (isUpdating) {
            }
        }
        ArrayList<EchoNode> echoNodes = new ArrayList<>();
        devices.forEach(((inetAddress, aBoolean) -> {
            if(aBoolean==false){
                echoNodes.add(Echo.getNode(inetAddress.getHostAddress()));
            }
        }));
        return echoNodes;
    }

    public int getNumOfDevice(){
        {
            if(isUpdating) System.out.println("DevicesList is updating...");
            while (isUpdating) {
            }
        }
        return devices.size();
    }

    @Override
    public String toString() {
        String ret= new String();
        ret += "-------------------------\n";
        ret += "Devices\n";
        ret += "-------------------------\n";
        for(InetAddress inetAddress : devices.keySet()){
            ret += "|"+inetAddress.getHostAddress()+"\t|"+devices.get(inetAddress)+"\t|\n";
        }
        ret += "-------------------------\n";
        return ret;
    }
}
