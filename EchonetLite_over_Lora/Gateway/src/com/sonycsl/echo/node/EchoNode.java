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
package com.sonycsl.echo.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoSocket;
import com.sonycsl.echo.EchoUtils;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.managementoperation.*;
import com.sonycsl.echo.eoj.device.sensor.*;
import com.sonycsl.echo.eoj.profile.*;
import com.sonycsl.echo.eoj.profile.NodeProfile.Proxy;


public final class EchoNode {
	private static HashMap<Short, DeviceProxyCreator> mProxyCreators = new HashMap<Short, DeviceProxyCreator>();
	
	private NodeProfile mNodeProfile;
	private List<DeviceObject> mDevices = new ArrayList<DeviceObject>();
	private String mAddress;
	
	public EchoNode(NodeProfile nodeProfile, DeviceObject[] devices) {
		// selfNode
		mAddress = EchoSocket.SELF_ADDRESS;
		mNodeProfile = nodeProfile;
		for(DeviceObject d : devices) {
			if(isSelfNode()) {
				d.allocateSelfDeviceInstanceCode();
			}
			mDevices.add(d);
		}
		
	}
	
	public EchoNode(String address) {
		// otherNode
		mAddress = address;
		mNodeProfile = new NodeProfile.Proxy();
	}

	public void onNew() {
		Echo.getEventListener().onNewNode(this);
	}
	
	public void onFound() {
		Echo.getEventListener().onFoundNode(this);
	}
	
	public boolean isSelfNode() {
		return EchoSocket.SELF_ADDRESS.equals(mAddress);
	}
	
	public boolean isProxy() {
		return !(EchoSocket.SELF_ADDRESS.equals(mAddress));
	}
	
	public InetAddress getAddress() {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(mAddress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return address;
	}
	
	public String getAddressStr() {
		return mAddress;
	}
	
	public NodeProfile getNodeProfile() {
		return mNodeProfile;
	}
	
	public DeviceObject addOtherDevice(short echoClassCode, byte echoInstanceCode) {
		DeviceObject device = newOtherDevice(echoClassCode, echoInstanceCode);
		addDevice(device);
		return device;
	}
	
	public void addDevice(DeviceObject device) {
		if(device == null) return;
		if(device.getNode() == this) return;

		mDevices.add(device);
		if(isSelfNode()) {
			device.allocateSelfDeviceInstanceCode();
			device.setNode(this);
			device.onNew();
			device.onFound();
		}
		/*
		short code = device.getEchoClassCode();
		if(mDeviceGroups.containsKey(code)) {
			List<DeviceObject> deviceList = mDeviceGroups.get(code);
			if(deviceList.size() > 0x7F) return;
			deviceList.add(device);
		} else {
			List<DeviceObject> deviceList = new ArrayList<DeviceObject>();
			deviceList.add(device);
			mDeviceGroups.put(code, deviceList);
		}
		if(mInitialized) {
			device.initialize(this);
		}*/
	}
	
	
	public void removeDevice(DeviceObject device) {
		if(device == null) return;
		if(device.getNode() != this) return;
		mDevices.remove(device);
	}
	
	public boolean containsDevice(short echoClassCode, byte echoInstanceCode) {
		for(DeviceObject d : mDevices) {
			if(d.getEchoClassCode() == echoClassCode
					&& d.getInstanceCode() == echoInstanceCode) {
				return true;
			}
		}
		return false;
	}

	public boolean containsDevice(DeviceObject device) {
		if(device == null) return false;
		if(device.getNode() != this) return false;
		return mDevices.contains(device);
	}

	public EchoObject getInstance(byte classGroupCode, byte classCode, byte instanceCode) {
		return getInstance(EchoUtils.getEchoClassCode(classGroupCode, classCode), instanceCode);
	}
	
	public EchoObject getInstance(short echoClassCode, byte echoInstanceCode) {
		if(mNodeProfile.getEchoClassCode() == echoClassCode
				&& mNodeProfile.getInstanceCode() == echoInstanceCode) {
			return mNodeProfile;
		}
		return getDevice(echoClassCode, echoInstanceCode);
	}
	
	
	public boolean containsInstance(byte classGroupCode, byte classCode, byte instanceCode) {
		short echoClassCode = EchoUtils.getEchoClassCode(classGroupCode, classCode);
		return containsInstance(echoClassCode, instanceCode);
	}
	
	public boolean containsInstance(short echoClassCode, byte echoInstanceCode) {
		if(mNodeProfile.getEchoClassCode() == echoClassCode
				&& mNodeProfile.getInstanceCode() == echoInstanceCode) {
			return true;
		}
	
		return containsDevice(echoClassCode, echoInstanceCode);
	}
	
	public DeviceObject getDevice(byte classGroupCode, byte classCode, byte instanceCode) {
		return getDevice(EchoUtils.getEchoClassCode(classGroupCode, classCode), instanceCode);
	}
	
	public DeviceObject getDevice(short echoClassCode, byte echoInstanceCode) {
		for(DeviceObject d : mDevices) {
			if(d.getEchoClassCode() == echoClassCode
					&& d.getInstanceCode() == echoInstanceCode) {
				return d;
			}
		}
		return null;
	}
	
	public DeviceObject[] getDevices(byte classGroupCode, byte classCode) {
		return getDevices(EchoUtils.getEchoClassCode(classGroupCode, classCode));
	}
	
	public DeviceObject[] getDevices(short echoClassCode) {
		List<DeviceObject> ret = new ArrayList<DeviceObject>();
		for(DeviceObject d : mDevices) {
			if(d.getEchoClassCode() == echoClassCode) {
				ret.add(d);
			}
		}
		return ret.toArray(new DeviceObject[]{});
	}
	
	public DeviceObject[] getDevices() {
		return mDevices.toArray(new DeviceObject[]{});
	}
	
	private static DeviceObject newOtherDevice(short echoClassCode, byte instanceCode) {
		if(mProxyCreators.containsKey(echoClassCode)) {
			return mProxyCreators.get(echoClassCode).create(instanceCode);
		}
		switch(echoClassCode) {
		case AirSpeedSensor.ECHO_CLASS_CODE: return new AirSpeedSensor.Proxy(instanceCode);
		case HumiditySensor.ECHO_CLASS_CODE: return new HumiditySensor.Proxy(instanceCode);
		case IlluminanceSensor.ECHO_CLASS_CODE: return new IlluminanceSensor.Proxy(instanceCode);
		case TemperatureSensor.ECHO_CLASS_CODE: return new TemperatureSensor.Proxy(instanceCode);
		case Controller.ECHO_CLASS_CODE: return new Controller.Proxy(instanceCode);
		case Switch.ECHO_CLASS_CODE: return new Switch.Proxy(instanceCode);
		case AgricultureSensor.ECHO_CLASS_CODE: return new AgricultureSensor.Proxy(instanceCode);
		default: return new DeviceObject.Proxy(echoClassCode, instanceCode);
		}
	}
	public static void putDeviceProxyCreator(short echoClassCode, DeviceProxyCreator creator) {
		mProxyCreators.put(echoClassCode, creator);
	}
	public static void removeDeviceProxyCreator(short echoClassCode) {
		mProxyCreators.remove(echoClassCode);
	}
	
	public interface DeviceProxyCreator {
		DeviceObject create(byte instanceCode);
	}

}
