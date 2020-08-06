package processor;
import com.sonycsl.echo.EchoFrame;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.node.EchoNode;
import dataStructure.DevicesList;
import utilites.Helper;

import java.io.IOException;
import java.util.ArrayList;

public class ReliableTransmitter implements Runnable{
    private Integer idLocalDevice;
    private Float timeToWater;
    private Thread currentThread;
    private Boolean isReceived;
    private Short currentTID;
    private Boolean result;
    final int timeToRetry=20000;
    public ReliableTransmitter(Integer idLocalDevice, Float timeToWater){
        this.idLocalDevice=idLocalDevice;
        this.timeToWater=timeToWater;
        result=false;
        isReceived=false;
        currentTID=-1;
    }
    @Override
    public void run() {
        //convert idLocalDevice -> deviceObject
        ArrayList<EchoNode> onlineNodes = DevicesList.getInstance().getNodesOnline();
        System.out.println(DevicesList.getInstance());
        DeviceObject currentDeviceObject=null;
        for(EchoNode echoNode : onlineNodes){
            for(DeviceObject deviceObj : echoNode.getDevices()){
                if(Helper.convertLoraIdToInt(deviceObj.getNode().getAddress()) == idLocalDevice){
                    currentDeviceObject = deviceObj;
                }
            }
            if(currentDeviceObject!=null) break;
        }
        //check device online: if device is offline, can't send packet
        if(currentDeviceObject == null){
//            System.out.println("ReliableTransmitter 44");
//            System.out.println("Device offline");
            result = false;
            return;
        }

        //get SetPacket response
        currentDeviceObject.setReceiver(new Switch.Receiver(){
            @Override
            protected void onSetTurnOnDuration(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                super.onSetTurnOnDuration(eoj, tid, esv, property, success);
//                System.out.println("Line 49 ReliableTransmitter.java");
//                System.out.println("Received response set packet "+property.edt[0]+" of TID: "+tid);
                if(tid==currentTID){
//                    System.out.println("isReceived in onSetTurnOnDuration(): "+isReceived);
                    isReceived=true;
                    result=true;
                }
            }
        });

        //Send
        while(true){
            if(Thread.interrupted()){
//                System.out.println("Thread "+Thread.currentThread().getId()+" is interrupted!!!!");
                break;
            }
            try {
                // send req Set packet
                if(currentDeviceObject instanceof Switch) {
                    Switch switchDevice = (Switch) currentDeviceObject;
//                    System.out.println(Thread.currentThread().getId()+" "+"SwitchDevice line 69 ReliableTransmitter.java "+switchDevice);

                    Switch.Setter currentSetter=null;
                    EchoFrame currentFrame=null;
                    if(currentTID == -1) {
                        currentSetter = switchDevice.set().reqSetTurnOnDuration(Helper.floatToByteArray(timeToWater));
                        currentFrame=currentSetter.send();
                        currentTID = currentFrame.getTID();
//                        System.out.println("line 84 Reliable Transmitter.java");
//                        System.out.println(Thread.currentThread().getId()+" "+"currentTID_first: "+currentTID);
                    }else{
                        if(currentSetter==null){
                            currentSetter=switchDevice.set().reqSetTurnOnDuration(Helper.floatToByteArray(timeToWater));
                        }
//                        System.out.println("line 90 Reliable Transmitter.java");
//                        System.out.println(Thread.currentThread().getId()+" "+"currentTID_before: "+currentTID);
                        currentTID = currentSetter.reSend(currentTID).getTID();
//                        System.out.println(Thread.currentThread().getId()+" "+"currentTID_after: "+currentTID);
                    }
                    //wait for response packet
                    Thread.sleep(timeToRetry);
                    //check response packet
//                    System.out.println(Thread.currentThread().getId()+" "+"isReceived in while(): "+isReceived);
                    if (isReceived) {
                        isReceived = false;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

        }
    }
}
