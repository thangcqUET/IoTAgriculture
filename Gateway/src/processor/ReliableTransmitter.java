package processor;

import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.node.EchoNode;
import dataStructure.DevicesList;
import utilites.Helper;

import java.io.IOException;
import java.util.ArrayList;

public class ReliableTransmitter {
    private Short TIDOfSentPacket[];
    private final Short maxNumOfTIDs = 100;// storage maximum numOfTIDs TIDs
    private Short numOfTIDs;
    private final Short timeToRetry = 5000;// milliseconds
    private final Short maxOfPacketResend = 5;
    private Boolean isReceived;
    ReliableTransmitter(){
        TIDOfSentPacket = new Short[maxNumOfTIDs];
        numOfTIDs = 0;
        isReceived = false;
    }

    public Boolean setC(Integer idLocalDevice, Float timeToWater){
        //convert idLocalDevice -> deviceObject
        ArrayList<EchoNode> onlineNodes = DevicesList.getInstance().getNodesOnline();
        System.out.println(DevicesList.getInstance());
        DeviceObject deviceObject=null;
        for(EchoNode echoNode : onlineNodes){
            for(DeviceObject deviceObj : echoNode.getDevices()){
                if(deviceObj.getEchoObjectCode() == idLocalDevice){
                    deviceObject = deviceObj;
                }
            }
            if(deviceObject!=null) break;
        }
        //check device online: if device is offline, can't send packet
        if(deviceObject == null) return false;

        // receive packet
        deviceObject.setReceiver(new Switch.Receiver(){
            @Override
            protected void onSetOperationStatus(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                super.onSetOperationStatus(eoj, tid, esv, property, success);
                System.out.println("Line 49 ReliableTransmitter.java");
                System.out.println("Received response set packet "+property.edt[0]+" of TID: "+tid);
                for (int i=0;i<numOfTIDs;i++){
                    if(tid == TIDOfSentPacket[i]){
                        isReceived = true;
                        break;
                    }
                }
            }
        });


        //Send
        int numOfPacketResend = 0;
        while(numOfPacketResend<=maxOfPacketResend){
            try {
                // send req Set packet
                if(deviceObject instanceof Switch) {
                    Switch switchDevice = new Switch.Proxy(deviceObject.getInstanceCode());
                    System.out.println("SwitchDevice line 69 ReliableTransmitter.java "+switchDevice);
                    Short TID = switchDevice.set().reqSetTurnOnDuration(Helper.floatToByteArray(timeToWater)).send().getTID();

                    TIDOfSentPacket[numOfTIDs++] = TID;
                    numOfPacketResend++;
                    Helper.printShorts(TIDOfSentPacket, numOfTIDs);
                    //wait for response packet
                    Thread.sleep(timeToRetry);
                    //check response packet
                    if (isReceived) {
                        numOfPacketResend = 0;
                        numOfTIDs = 0;
                        isReceived = false;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(numOfPacketResend>=maxOfPacketResend) return false;
        return true;
    }

    public Boolean setC(Integer idLocalDevice, Boolean status){
        //convert idLocalDevice -> deviceObject
        ArrayList<EchoNode> onlineNodes = DevicesList.getInstance().getNodesOnline();
        System.out.println(DevicesList.getInstance());
        DeviceObject deviceObject=null;
        for(EchoNode echoNode : onlineNodes){
            for(DeviceObject deviceObj : echoNode.getDevices()){
                if(deviceObj.getEchoObjectCode() == idLocalDevice){
                    deviceObject = deviceObj;
                }
            }
            if(deviceObject!=null) break;
        }
        //check device online: if device is offline, can't send packet
        if(deviceObject == null) return false;

        // receive packet
        deviceObject.setReceiver(new Switch.Receiver(){
            @Override
            protected void onSetOperationStatus(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                super.onSetOperationStatus(eoj, tid, esv, property, success);
                System.out.println("received response set packet "+property.edt[0]);
                for (int i=0;i<numOfTIDs;i++){
                    if(tid == TIDOfSentPacket[i]){
                        isReceived = true;
                        break;
                    }
                }
            }
        });

        int numOfPacketResend = 0;
        while(numOfPacketResend<=maxOfPacketResend){
            try {
                // send req Set packet
                Short TID = deviceObject.set().reqSetOperationStatus(
                        status == true?
                                DeviceObject.EDT_OPERATION_STATUS_ON:
                                DeviceObject.EDT_OPERATION_STATUS_OFF).send().getTID();
                TIDOfSentPacket[numOfTIDs++] = TID;
                numOfPacketResend++;
                //wait for response packet
                Thread.sleep(timeToRetry);
                Helper.printShorts(TIDOfSentPacket,numOfTIDs);
                //check response packet
                if(isReceived){
                    numOfPacketResend = 0;
                    numOfTIDs = 0;
                    isReceived = false;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(numOfPacketResend>=maxOfPacketResend) return false;
        return true;
    }
}
