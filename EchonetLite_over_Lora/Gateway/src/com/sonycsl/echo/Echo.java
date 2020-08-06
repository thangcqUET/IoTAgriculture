/*
 * Copyright 2012 Sony Computer Science Laboratories, Inc. <info@kadecot.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sonycsl.echo;

import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.Controller;
import com.sonycsl.echo.eoj.device.managementoperation.Switch;
import com.sonycsl.echo.eoj.device.sensor.*;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.eoj.profile.ProfileObject;
import com.sonycsl.echo.node.EchoNode;

import java.io.IOException;
import java.io.PrintStream;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Echo {

    private static volatile EchoNode sSelfNode;
    private static Map<String, EchoNode> sOtherNodes;

    private static Events sEvents = null;
    private static ArrayList<EventListener> sListeners;

    private volatile static boolean sStarted = false;
    private volatile static boolean sCleared = true;

    static {
        sOtherNodes = new ConcurrentHashMap<String, EchoNode>();
        sListeners = new ArrayList<EventListener>();
        sEvents = new Events();
    }

    private Echo() {
    }

    public synchronized static EchoNode start(NodeProfile profile, DeviceObject[] devices) throws IOException {
        if (sStarted)
            return null;
        if (!sCleared)
            return null;

        sStarted = true;
        sCleared = false;

        sSelfNode = new EchoNode(profile, devices);
        profile.setNode(sSelfNode);
        for (DeviceObject dev : devices) {
            dev.setNode(sSelfNode);
        }
        EchoSocket.openSocket();

        return postOpenSocket(devices);
    }

    public synchronized static EchoNode start(NodeProfile profile, DeviceObject[] devices, String infName)
            throws IOException {
        if (sStarted)
            return null;
        if (!sCleared)
            return null;

        sStarted = true;
        sCleared = false;

        sSelfNode = new EchoNode(profile, devices);
        profile.setNode(sSelfNode);
        for (DeviceObject dev : devices) {
            dev.setNode(sSelfNode);
        }
        EchoSocket.openSocket();

        return postOpenSocket(devices);
    }

    private static EchoNode postOpenSocket(DeviceObject[] devices) throws IOException {
        // Echo.getEventListener().onNewNode(sSelfNode);
        sSelfNode.onNew();
        // Echo.getEventListener().onFoundNode(sSelfNode);
        sSelfNode.onFound();
        sSelfNode.getNodeProfile().onNew();
        sSelfNode.getNodeProfile().onFound();

        for (DeviceObject dev : devices) {
            dev.onNew();
            dev.onFound();
        }

        sSelfNode.getNodeProfile().inform().reqInformInstanceListNotification().send();
        return sSelfNode;
    }

    public synchronized static void restart() throws IOException {
        if (sCleared)
            return;
        sStarted = true;
        EchoSocket.openSocket();
        sSelfNode.getNodeProfile().inform().reqInformInstanceListNotification().send();
    }

    public synchronized static void stop() throws IOException {
        System.err.println("Echo stop");
        EchoSocket.closeSocket();
        sStarted = false;
        System.err.println("Echo closed");
        // sNodes.clear();
    }

    public synchronized static void clear() throws IOException {
        stop();
        sCleared = true;

        sSelfNode = null;

        sOtherNodes.clear();
        sListeners.clear();
    }

    public static boolean isStarted() {
        return sStarted;
    }

    // remain for back compatibility.
    @Deprecated
    public static EchoNode getNode() {
        return getSelfNode();
    }

    public static EchoNode getSelfNode() {
        return sSelfNode;
    }

    public static EchoNode[] getNodes() {
        Collection<EchoNode> nodes = sOtherNodes.values();
        List<EchoNode> ret = new ArrayList<EchoNode>();
        if (sSelfNode != null) {
            ret.add(sSelfNode);
        }
        for (EchoNode n : nodes) {
            ret.add(n);
        }
        return ret.toArray(new EchoNode[] {});
    }

    public static EchoNode getNode(String address) {
        if (EchoSocket.SELF_ADDRESS.equals(address)) {
            return sSelfNode;
        }
        return sOtherNodes.get(address);
    }

    public synchronized static EchoNode addOtherNode(String address) {
        EchoNode node = new EchoNode(address);
        node.getNodeProfile().setNode(node);
        sOtherNodes.put(address, node);

        return node;
    }

    public static void removeOtherNode(String address) {
        sOtherNodes.remove(address);
    }

    // public static void removeAllNode() {
    // sNodes.clear();
    // }

    /*
     * public synchronized static EchoNode[] getActiveNodes() {
     * Collection<EchoNode> nodes = sOtherNodes.values(); List<EchoNode> ret =
     * new ArrayList<EchoNode>(); if(sSelfNode != null && sSelfNode.isActive()){
     * ret.add(sSelfNode); } for(EchoNode n : nodes) { if(n.isActive())
     * ret.add(n); } return ret.toArray(new EchoNode[]{}); }
     */

    // @Deprecated
    // public static EchoObject getInstance(InetAddress address, byte
    // classGroupCode, byte classCode, byte instanceCode) {
    // return getInstance(address, EchoUtils.getEchoClassCode(classGroupCode,
    // classCode), instanceCode);
    // }
    //
    // @Deprecated
    // public static EchoObject getInstance(InetAddress address, int
    // objectCode){
    // return getInstance(address,
    // EchoUtils.getEchoClassCodeFromObjectCode(objectCode),
    // EchoUtils.getInstanceCodeFromObjectCode(objectCode));
    // }
    //
    // @Deprecated
    // public static EchoObject getInstance(InetAddress address, short
    // echoClassCode, byte instanceCode) {
    //
    // if(sCleared) {
    // return null;
    // }
    // if(address == null) {
    // return null;
    // }
    // if(address.equals(sSelfNode.getAddressStr())) {
    // if(!sSelfNode.containsInstance(echoClassCode, instanceCode)) return null;
    // return sSelfNode.getInstance(echoClassCode, instanceCode);
    // } else if(sOtherNodes.containsKey(address)) {
    // EchoNode node = sOtherNodes.get(address);
    // if(!node.containsInstance(echoClassCode, instanceCode)) return null;
    // return node.getInstance(echoClassCode, instanceCode);
    // } else {
    // return null;
    // }
    // }
    /*
     * public synchronized static void updateNodeInstance(InetAddress address,
     * byte classGroupCode, byte classCode, byte instanceCode) { if(sCleared) {
     * return; } if(address == null) { return; }
     * if(address.equals(sSelfNode.getAddress())) {
     * //if(sLocalNode.containsInstance(classGroupCode, classCode,
     * instanceCode)) return;
     * //sLocalNode.addDevice(EchoUtils.getEchoClassCode(classGroupCode,
     * classCode), instanceCode); if(sSelfNode.containsInstance(classGroupCode,
     * classCode, instanceCode)) { sSelfNode.getInstance(classGroupCode,
     * classCode, instanceCode).setActive(true); return; } } else
     * if(sOtherNodes.containsKey(address)) { EchoNode node =
     * sOtherNodes.get(address); if(node.containsInstance(classGroupCode,
     * classCode, instanceCode)){ node.getInstance(classGroupCode, classCode,
     * instanceCode).setActive(true); return; } else {
     * node.addDevice(EchoUtils.getEchoClassCode(classGroupCode, classCode),
     * instanceCode); } } else { if(NodeProfile.ECHO_CLASS_CODE ==
     * EchoUtils.getEchoClassCode(classGroupCode, classCode) &&
     * NodeProfile.INSTANCE_CODE == instanceCode) { new EchoNode(address, new
     * ArrayList<Integer>()); } else { ArrayList<Integer> list = new
     * ArrayList<Integer>();
     * list.add(EchoUtils.getEchoObjectCode(classGroupCode, classCode,
     * instanceCode)); new EchoNode(address, list); } } }
     */
    // public synchronized static void updateNodeDevices(InetAddress address,
    // List<Integer> echoObjectCodeList) {
    // if(echoObjectCodeList == null) return;
    /*
     * if(sLocalNode.getAddress().equals(address)) {
     * //sLocalNode.updateDevices(echoObjectCodeList); return; }else
     * if(sNodes.containsKey(address)) { EchoNode node = sNodes.get(address);
     * node.updateDevices(echoObjectCodeList); } else { new EchoNode(address,
     * echoObjectCodeList); }
     */
    /*
     * if(sCleared) { return; } if(address == null) { return; }
     * if(!address.equals(sSelfNode.getAddress()) &&
     * !sOtherNodes.containsKey(address)) { new EchoNode(address,
     * echoObjectCodeList); return; } for(int objCode: echoObjectCodeList) {
     * byte[] a = EchoUtils.toByteArray(objCode, 4); updateNodeInstance(address,
     * a[1],a[2],a[3]); } if(!sOtherNodes.containsKey(address)) return;
     * for(DeviceObject dev : sOtherNodes.get(address).getDevices()) { boolean
     * active = false; for(int code : echoObjectCodeList) { if(code ==
     * dev.getEchoObjectCode()) { active = true; break; } }
     * dev.setActive(active); } }
     */

    public static void addEventListener(EventListener listener) {
        sListeners.add(listener);
    }

    public static EventListener getEventListener() {
        return sEvents;
    }

    public static class EventListener {
        public  void onNewAgricultureSensor(AgricultureSensor device){}

        public void setProperty(EchoObject eoj, EchoProperty property, boolean success) {
        }

        public void getProperty(EchoObject eoj, EchoProperty property) {
        }

        public void isValidProperty(EchoObject eoj, EchoProperty property, boolean valid) {
        }

        public void onSetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property,
                boolean success) {
        }

        public void onGetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property,
                boolean success) {
        }

        public void onInformProperty(EchoObject eoj, short tid, byte esv, EchoProperty property) {
        }

        public void reqSetPropertyEvent(EchoObject eoj, EchoProperty property) {
        }

        public void reqGetPropertyEvent(EchoObject eoj, EchoProperty property) {
        }

        public void reqInformPropertyEvent(EchoObject eoj, EchoProperty property) {
        }

        public void reqInformCPropertyEvent(EchoObject eoj, EchoProperty property) {
        }

        public void sendEvent(EchoFrame frame) {
        }

        public void receiveEvent(EchoFrame frame) {
        }

        public void onCatchException(Exception e) {
        }

        public void onFoundNode(EchoNode node) {
        }

        public void onFoundEchoObject(EchoObject eoj) {
        }

        public void onNewNode(EchoNode node) {
        }

        public void onNewEchoObject(EchoObject eoj) {
        }

        public void onNewProfileObject(ProfileObject profile) {
        }

        public void onNewNodeProfile(NodeProfile profile) {
        }

        public void onNewDeviceObject(DeviceObject device) {
        }

        public void onNewAirSpeedSensor(AirSpeedSensor device) {
        }

        public void onNewHumiditySensor(HumiditySensor device) {
        }

        public void onNewIlluminanceSensor(IlluminanceSensor device) {
        }

        public void onNewTemperatureSensor(TemperatureSensor device) {
        }

        public void onNewController(Controller device) {
        }

        public void onNewSwitch(Switch device) {
        }
    }

    public static class Logger extends EventListener {
        PrintStream mOut;

        public Logger(PrintStream out) {
            mOut = out;
        }

        @Override
        public void setProperty(EchoObject eoj, EchoProperty property,
                boolean success) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:set," + eoj.toString()
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt)
                    + ",success:" + success);
        }

        @Override
        public void getProperty(EchoObject eoj, EchoProperty property) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:get," + eoj.toString()
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt));
        }

        @Override
        public void isValidProperty(EchoObject eoj, EchoProperty property,
                boolean valid) {
            // TODO Auto-generated method stub
            super.isValidProperty(eoj, property, valid);
        }

        @Override
        public void onSetProperty(EchoObject eoj, short tid, byte esv,
                EchoProperty property, boolean success) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:onSet," + eoj.toString()
                    + ",tid:" + EchoUtils.toHexString(tid)
                    + ",esv:" + EchoUtils.toHexString(esv)
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt)
                    + ",success:" + success);
        }

        @Override
        public void onGetProperty(EchoObject eoj, short tid, byte esv,
                EchoProperty property, boolean success) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:onGet," + eoj.toString()
                    + ",tid:" + EchoUtils.toHexString(tid)
                    + ",esv:" + EchoUtils.toHexString(esv)
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt));

        }

        @Override
        public void onInformProperty(EchoObject eoj, short tid, byte esv,
                EchoProperty property) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:onInform," + eoj.toString()
                    + ",tid:" + EchoUtils.toHexString(tid)
                    + ",esv:" + EchoUtils.toHexString(esv)
                    + ",epc:" + EchoUtils.toHexString(property.epc));
        }

        @Override
        public void reqSetPropertyEvent(EchoObject eoj, EchoProperty property) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:reqSet," + eoj.toString()
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt));
        }

        @Override
        public void reqGetPropertyEvent(EchoObject eoj, EchoProperty property) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:reqGet," + eoj.toString()
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt));
        }

        @Override
        public void reqInformPropertyEvent(EchoObject eoj, EchoProperty property) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:reqInform," + eoj.toString()
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt));
        }

        @Override
        public void reqInformCPropertyEvent(EchoObject eoj,
                EchoProperty property) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:reqInformC," + eoj.toString()
                    + ",epc:" + EchoUtils.toHexString(property.epc)
                    + ",pdc:" + EchoUtils.toHexString(property.pdc)
                    + ",edt:" + EchoUtils.toHexString(property.edt));
        }

        @Override
        public void sendEvent(EchoFrame frame) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:send,tid:" + EchoUtils.toHexString(frame.getTID())
                    + ",esv:" + EchoUtils.toHexString(frame.getESV())
                    + ",seoj:[class:" + String.format("%04x", frame.getSrcEchoClassCode())
                    + ",instance:" + String.format("%02x", frame.getSrcEchoInstanceCode())
                    + "],deoj:[class:" + String.format("%04x", frame.getDstEchoClassCode())
                    + ",instance:" + String.format("%02x", frame.getDstEchoInstanceCode())
                    + "],data:" + EchoUtils.toHexString(frame.getFrameByteArray()));
        }

        @Override
        public void receiveEvent(EchoFrame frame) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:receive,tid:" + EchoUtils.toHexString(frame.getTID())
                    + ",esv:" + EchoUtils.toHexString(frame.getESV())
                    + ",seoj:[class:" + String.format("%04x", frame.getSrcEchoClassCode())
                    + ",instance:" + String.format("%02x", frame.getSrcEchoInstanceCode())
                    + "],deoj:[class:" + String.format("%04x", frame.getDstEchoClassCode())
                    + ",instance:" + String.format("%02x", frame.getDstEchoInstanceCode())
                    + "],data:" + EchoUtils.toHexString(frame.getFrameByteArray()));
        }

        @Override
        public void onCatchException(Exception e) {
            // TODO Auto-generated method stub
            super.onCatchException(e);
        }

        @Override
        public void onNewNode(EchoNode node) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:new,type:node,address:"
                    + node.getAddressStr());
        }

        @Override
        public void onNewEchoObject(EchoObject eoj) {
            long millis = System.currentTimeMillis();
            mOut.println("millis:" + millis
                    + ",method:new,type:eoj,"
                    + eoj.toString());
        }

    }

    private static class Events extends EventListener {

        @Override
        public void setProperty(EchoObject eoj, EchoProperty property,
                boolean success) {
            super.setProperty(eoj, property, success);
            for (EventListener listener : sListeners) {
                listener.setProperty(eoj, property, success);
            }
        }

        @Override
        public void getProperty(EchoObject eoj, EchoProperty property) {
            for (EventListener listener : sListeners) {
                listener.getProperty(eoj, property);
            }
        }

        @Override
        public void isValidProperty(EchoObject eoj, EchoProperty property,
                boolean valid) {
            for (EventListener listener : sListeners) {
                listener.isValidProperty(eoj, property, valid);
            }
        }

        @Override
        public void onSetProperty(EchoObject eoj, short tid, byte esv,
                EchoProperty property, boolean success) {
            for (EventListener listener : sListeners) {
                listener.onSetProperty(eoj, tid, esv, property, success);
            }
        }

        @Override
        public void onGetProperty(EchoObject eoj, short tid, byte esv,
                EchoProperty property, boolean success) {
            for (EventListener listener : sListeners) {
                listener.onGetProperty(eoj, tid, esv, property, success);
            }
        }

        @Override
        public void onInformProperty(EchoObject eoj, short tid, byte esv,
                EchoProperty property) {
            for (EventListener listener : sListeners) {
                listener.onInformProperty(eoj, tid, esv, property);
            }
        }

        @Override
        public void reqSetPropertyEvent(EchoObject eoj, EchoProperty property) {
            for (EventListener listener : sListeners) {
                listener.reqSetPropertyEvent(eoj, property);
            }
        }

        @Override
        public void reqGetPropertyEvent(EchoObject eoj, EchoProperty property) {
            for (EventListener listener : sListeners) {
                listener.reqGetPropertyEvent(eoj, property);
            }
        }

        @Override
        public void reqInformPropertyEvent(EchoObject eoj, EchoProperty property) {
            for (EventListener listener : sListeners) {
                listener.reqInformPropertyEvent(eoj, property);
            }
        }

        @Override
        public void reqInformCPropertyEvent(EchoObject eoj,
                EchoProperty property) {
            for (EventListener listener : sListeners) {
                listener.reqInformCPropertyEvent(eoj, property);
            }
        }

        @Override
        public void sendEvent(EchoFrame frame) {
            for (EventListener listener : sListeners) {
                listener.sendEvent(frame);
            }
        }

        @Override
        public void receiveEvent(EchoFrame frame) {
            for (EventListener listener : sListeners) {
                listener.receiveEvent(frame);
            }
        }

        @Override
        public void onCatchException(Exception e) {
            for (EventListener listener : sListeners) {
                listener.onCatchException(e);
            }
        }

        @Override
        public void onFoundNode(EchoNode node) {
            for (EventListener listener : sListeners) {
                listener.onFoundNode(node);
            }
        }

        @Override
        public void onFoundEchoObject(EchoObject eoj) {
            for (EventListener listener : sListeners) {
                listener.onFoundEchoObject(eoj);
            }
        }

        @Override
        public void onNewNode(EchoNode node) {
            for (EventListener listener : sListeners) {
                listener.onNewNode(node);
            }
        }

        @Override
        public void onNewEchoObject(EchoObject eoj) {
            for (EventListener listener : sListeners) {
                listener.onNewEchoObject(eoj);
            }
        }

        @Override
        public void onNewProfileObject(ProfileObject profile) {
            for (EventListener listener : sListeners) {
                listener.onNewProfileObject(profile);
            }
        }

        @Override
        public void onNewNodeProfile(NodeProfile profile) {
            for (EventListener listener : sListeners) {
                listener.onNewNodeProfile(profile);
            }
        }

        @Override
        public void onNewDeviceObject(DeviceObject device) {
            for (EventListener listener : sListeners) {
                listener.onNewDeviceObject(device);
            }
        }

        @Override
        public void onNewAirSpeedSensor(AirSpeedSensor device) {
            for (EventListener listener : sListeners) {
                listener.onNewAirSpeedSensor(device);
            }
        }

        @Override
        public void onNewHumiditySensor(HumiditySensor device) {
            for (EventListener listener : sListeners) {
                listener.onNewHumiditySensor(device);
            }
        }

        @Override
        public void onNewAgricultureSensor(AgricultureSensor device) {
            for (EventListener listener: sListeners){
                listener.onNewAgricultureSensor(device);
            }
        }

        @Override
        public void onNewIlluminanceSensor(IlluminanceSensor device) {
            for (EventListener listener : sListeners) {
                listener.onNewIlluminanceSensor(device);
            }
        }

        @Override
        public void onNewTemperatureSensor(TemperatureSensor device) {
            for (EventListener listener : sListeners) {
                listener.onNewTemperatureSensor(device);
            }
        }

        @Override
        public void onNewController(Controller device) {
            for (EventListener listener : sListeners) {
                listener.onNewController(device);
            }
        }

        @Override
        public void onNewSwitch(Switch device) {
            for (EventListener listener : sListeners) {
                listener.onNewSwitch(device);
            }
        }

    }
}
