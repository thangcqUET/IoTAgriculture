package com.sonycsl.echo.eoj.device.sensor;

import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.EchoSocket;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;

public abstract class AgricultureSensor extends DeviceObject {
    public static final short ECHO_CLASS_CODE = (short)0x002E;

    public static final byte EPC_AIR_TEMPERATURE_VALUE = (byte)0xF0;
    public static final byte EPC_SOIL_TEMPERATURE_VALUE = (byte)0xF1;
    public static final byte EPC_AIR_HUMIDITY_VALUE = (byte)0xF2;
    public static final byte EPC_SOIL_MOISTURE_VALUE = (byte)0xF3;
    public static final byte EPC_LIGHT_LEVEL_VALUE = (byte)0xF4;

    @Override
    protected void setupPropertyMaps() {
        super.setupPropertyMaps();

        addStatusChangeAnnouncementProperty(EPC_OPERATION_STATUS);
        removeSetProperty(EPC_OPERATION_STATUS);
        addGetProperty(EPC_OPERATION_STATUS);
        addGetProperty(EPC_AIR_TEMPERATURE_VALUE);
        addGetProperty(EPC_SOIL_TEMPERATURE_VALUE);
        addGetProperty(EPC_AIR_HUMIDITY_VALUE);
        addGetProperty(EPC_SOIL_MOISTURE_VALUE);
        addGetProperty(EPC_LIGHT_LEVEL_VALUE);
    }

    @Override
    public void onNew() {
        super.onNew();
        Echo.getEventListener().onNewAgricultureSensor(this);
    }

    @Override
    public short getEchoClassCode() {
        return ECHO_CLASS_CODE;
    }

    /**
     * Property name : Operation status<br>
     * <br>
     * EPC : 0x80<br>
     * <br>
     * Contents of property :<br>
     * This property indicates the ON/OFF status.<br>
     * <br>
     * Value range (decimal notation) :<br>
     * ON=0x30, OFF=0x31<br>
     * <br>
     * Data type : unsigned char<br>
     * <br>
     * Data size : 1 byte<br>
     * <br>
     * Unit : —<br>
     * <br>
     * Access rule :<br>
     * Announce - undefined<br>
     * Set - optional<br>
     * Get - mandatory<br>
     * <br>
     * <b>Announcement at status change</b><br>
     */
    protected boolean setOperationStatus(byte[] edt) {return false;}
    /**
     * Property name : Operation status<br>
     * <br>
     * EPC : 0x80<br>
     * <br>
     * Contents of property :<br>
     * This property indicates the ON/OFF status.<br>
     * <br>
     * Value range (decimal notation) :<br>
     * ON=0x30, OFF=0x31<br>
     * <br>
     * Data type : unsigned char<br>
     * <br>
     * Data size : 1 byte<br>
     * <br>
     * Unit : —<br>
     * <br>
     * Access rule :<br>
     * Announce - undefined<br>
     * Set - optional<br>
     * Get - mandatory<br>
     * <br>
     * <b>Announcement at status change</b><br>
     */
    protected abstract byte[] getOperationStatus();
    /**
     * Property name : Measured temperature value<br>
     * <br>
     * EPC : 0xE0<br>
     * <br>
     * Contents of property :<br>
     * This property indicates the measured temperature value in units of 0.1.C.<br>
     * <br>
     * Value range (decimal notation) :<br>
     * 0xF554.0x7FFE (-2732.32766) (-273.2.3276.6.C)<br>
     * <br>
     * Data type : signed short<br>
     * <br>
     * Data size : 2 bytes<br>
     * <br>
     * Unit : 0.1.C<br>
     * <br>
     * Access rule :<br>
     * Announce - undefined<br>
     * Set - undefined<br>
     * Get - mandatory<br>
     */
    protected abstract byte[] getAirTemperature();
    protected abstract byte[] getSoilTemperature();
    protected abstract byte[] getAirHumidity();
    protected abstract byte[] getSoilMoisture();
    protected abstract byte[] getLightLevel();

    protected boolean setAirTemperature(byte[] edt) {
        return false;
    }
    protected boolean setSoilTemperature(byte[] edt) {
        return false;
    }
    protected boolean setAirHumidity(byte[] edt) {
        return false;
    }
    protected boolean setSoilMoisture(byte[] edt) {
        return false;
    }
    protected boolean setLightLevel(byte[] edt) {
        return false;
    }
    /**
     * Property name : Measured temperature value<br>
     * <br>
     * EPC : 0xE0<br>
     * <br>
     * Contents of property :<br>
     * This property indicates the measured temperature value in units of 0.1.C.<br>
     * <br>
     * Value range (decimal notation) :<br>
     * 0xF554.0x7FFE (-2732.32766) (-273.2.3276.6.C)<br>
     * <br>
     * Data type : signed short<br>
     * <br>
     * Data size : 2 bytes<br>
     * <br>
     * Unit : 0.1.C<br>
     * <br>
     * Access rule :<br>
     * Announce - undefined<br>
     * Set - undefined<br>
     * Get - mandatory<br>
     */
    protected boolean isValidAirTemperatureValue(byte[] edt) {
        if(edt == null || !(edt.length == 4)) return false;
        return true;
    }
    protected boolean isValidSoilTemperatureValue(byte[] edt){
        if (edt == null || !(edt.length == 4)) return false;
        return true;
    }
    protected boolean isValidAirHumidityValue(byte[] edt){
        if (edt == null || !(edt.length == 4)) return false;
        return true;
    }
    protected boolean isValidSoilMoistureValue(byte[] edt){
        if (edt == null || !(edt.length == 4)) return false;
        return true;
    }
    protected boolean isValidLightLevelValue(byte[] edt){
        if (edt == null || !(edt.length == 4)) return false;
        return true;
    }

    @Override
    protected synchronized boolean setProperty(EchoProperty property) {
        boolean success = super.setProperty(property);
        if(success) return success;

        switch(property.epc) {
            case EPC_AIR_TEMPERATURE_VALUE : return setAirTemperature(property.edt);
            case EPC_SOIL_TEMPERATURE_VALUE : return setSoilTemperature(property.edt);
            case EPC_AIR_HUMIDITY_VALUE : return setAirHumidity(property.edt);
            case EPC_SOIL_MOISTURE_VALUE : return setSoilMoisture(property.edt);
            case EPC_LIGHT_LEVEL_VALUE : return setLightLevel(property.edt);
            default : return false;
        }
    }

    @Override
    protected synchronized byte[] getProperty(byte epc) {
        byte[] edt = super.getProperty(epc);
        if(edt != null) return edt;

        switch(epc) {
            case EPC_AIR_TEMPERATURE_VALUE : return getAirTemperature();
            case EPC_SOIL_TEMPERATURE_VALUE: return getSoilTemperature();
            case EPC_AIR_HUMIDITY_VALUE: return getAirHumidity();
            case EPC_SOIL_MOISTURE_VALUE: return getSoilMoisture();
            case EPC_LIGHT_LEVEL_VALUE: return getLightLevel();
            default : return null;
        }
    }

    @Override
    protected synchronized boolean isValidProperty(EchoProperty property) {
        boolean valid = super.isValidProperty(property);
        if(valid) return valid;

        switch(property.epc) {
            case EPC_AIR_TEMPERATURE_VALUE : return isValidAirTemperatureValue(property.edt);
            case EPC_SOIL_TEMPERATURE_VALUE: return isValidSoilTemperatureValue(property.edt);
            case EPC_AIR_HUMIDITY_VALUE: return isValidAirHumidityValue(property.edt);
            case EPC_SOIL_MOISTURE_VALUE: return isValidSoilMoistureValue(property.edt);
            case EPC_LIGHT_LEVEL_VALUE: return isValidLightLevelValue(property.edt);
            default : return false;
        }
    }

    @Override
    public Setter set() {
        return set(true);
    }

    @Override
    public Setter set(boolean responseRequired) {
        return new Setter(getEchoClassCode(), getInstanceCode()
                , getNode().getAddressStr(), responseRequired);
    }

    @Override
    public Getter get() {
        return new Getter(getEchoClassCode(), getInstanceCode()
                , getNode().getAddressStr());
    }

    @Override
    public Informer inform() {
        return inform(isSelfObject());
    }

    @Override
    protected Informer inform(boolean multicast) {
        String address;
        if(multicast) {
            address = EchoSocket.MULTICAST_ADDRESS;
        } else {
            address = getNode().getAddressStr();
        }
        return new Informer(getEchoClassCode(), getInstanceCode()
                , address, isSelfObject());
    }

    public static class Receiver extends DeviceObject.Receiver {

        @Override
        protected boolean onSetProperty(EchoObject eoj, short tid, byte esv,
                                        EchoProperty property, boolean success) {
            boolean ret = super.onSetProperty(eoj, tid, esv, property, success);
            if(ret) return true;

            switch(property.epc) {
                case EPC_AIR_TEMPERATURE_VALUE : 
                    onSetAirTemperature( eoj, tid, esv, property, success);
                    return true;
                case EPC_SOIL_TEMPERATURE_VALUE :
                    onSetSoilTemperature( eoj, tid, esv, property, success);
                    return true;
                case EPC_AIR_HUMIDITY_VALUE : 
                    onSetAirHumidity( eoj, tid, esv, property, success);
                    return true;
                case EPC_SOIL_MOISTURE_VALUE :
                    onSetSoilMoisture( eoj, tid, esv, property, success);
                    return true;
                case EPC_LIGHT_LEVEL_VALUE :
                    onSetLightLevel( eoj, tid, esv, property, success);
                    return true;
                default :
                    return false;
            }
        }

        @Override
        protected boolean onGetProperty(EchoObject eoj, short tid, byte esv,
                                        EchoProperty property, boolean success) {
            boolean ret = super.onGetProperty(eoj, tid, esv, property, success);
            if(ret) return true;

            switch(property.epc) {
                case EPC_AIR_TEMPERATURE_VALUE :
                    onGetAirTemperature(eoj, tid, esv, property, success);
                    return true;
                case EPC_SOIL_TEMPERATURE_VALUE :
                    onGetSoilTemperature( eoj, tid, esv, property, success);
                    return true;
                case EPC_AIR_HUMIDITY_VALUE :
                    onGetAirHumidity( eoj, tid, esv, property, success);
                    return true;
                case EPC_SOIL_MOISTURE_VALUE :
                    onGetSoilMoisture( eoj, tid, esv, property, success);
                    return true;
                case EPC_LIGHT_LEVEL_VALUE :
                    onGetLightLevel( eoj, tid, esv, property, success);
                    return true;    
                default :
                    return false;
            }
        }

        /**
         * Property name : Measured temperature value<br>
         * <br>
         * EPC : 0xE0<br>
         * <br>
         * Contents of property :<br>
         * This property indicates the measured temperature value in units of 0.1.C.<br>
         * <br>
         * Value range (decimal notation) :<br>
         * 0xF554.0x7FFE (-2732.32766) (-273.2.3276.6.C)<br>
         * <br>
         * Data type : signed short<br>
         * <br>
         * Data size : 2 bytes<br>
         * <br>
         * Unit : 0.1.C<br>
         * <br>
         * Access rule :<br>
         * Announce - undefined<br>
         * Set - undefined<br>
         * Get - mandatory<br>
         */
        protected void onGetAirTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onGetSoilTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onGetAirHumidity(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onGetSoilMoisture(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onGetLightLevel(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}

        protected void onSetAirTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onSetSoilTemperature(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onSetAirHumidity(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onSetSoilMoisture(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}
        protected void onSetLightLevel(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {}

    }

    public static class Setter extends DeviceObject.Setter {
        public Setter(short dstEchoClassCode, byte dstEchoInstanceCode
                , String dstEchoAddress, boolean responseRequired) {
            super(dstEchoClassCode, dstEchoInstanceCode
                    , dstEchoAddress, responseRequired);
        }

        @Override
        public Setter reqSetProperty(byte epc, byte[] edt) {
            return (Setter)super.reqSetProperty(epc, edt);
        }

        @Override
        public Setter reqSetOperationStatus(byte[] edt) {
            return (Setter)super.reqSetOperationStatus(edt);
        }
        @Override
        public Setter reqSetInstallationLocation(byte[] edt) {
            return (Setter)super.reqSetInstallationLocation(edt);
        }
        @Override
        public Setter reqSetCurrentLimitSetting(byte[] edt) {
            return (Setter)super.reqSetCurrentLimitSetting(edt);
        }
        @Override
        public Setter reqSetPowerSavingOperationSetting(byte[] edt) {
            return (Setter)super.reqSetPowerSavingOperationSetting(edt);
        }
        @Override
        public Setter reqSetRemoteControlSetting(byte[] edt) {
            return (Setter)super.reqSetRemoteControlSetting(edt);
        }
        @Override
        public Setter reqSetCurrentTimeSetting(byte[] edt) {
            return (Setter)super.reqSetCurrentTimeSetting(edt);
        }
        @Override
        public Setter reqSetCurrentDateSetting(byte[] edt) {
            return (Setter)super.reqSetCurrentDateSetting(edt);
        }
        @Override
        public Setter reqSetPowerLimitSetting(byte[] edt) {
            return (Setter)super.reqSetPowerLimitSetting(edt);
        }

    }

    public static class Getter extends DeviceObject.Getter {
        public Getter(short dstEchoClassCode, byte dstEchoInstanceCode
                , String dstEchoAddress) {
            super(dstEchoClassCode, dstEchoInstanceCode
                    , dstEchoAddress);
        }

        @Override
        public Getter reqGetProperty(byte epc) {
            return (Getter)super.reqGetProperty(epc);
        }

        @Override
        public Getter reqGetOperationStatus() {
            return (Getter)super.reqGetOperationStatus();
        }
        @Override
        public Getter reqGetInstallationLocation() {
            return (Getter)super.reqGetInstallationLocation();
        }
        @Override
        public Getter reqGetStandardVersionInformation() {
            return (Getter)super.reqGetStandardVersionInformation();
        }
        @Override
        public Getter reqGetIdentificationNumber() {
            return (Getter)super.reqGetIdentificationNumber();
        }
        @Override
        public Getter reqGetMeasuredInstantaneousPowerConsumption() {
            return (Getter)super.reqGetMeasuredInstantaneousPowerConsumption();
        }
        @Override
        public Getter reqGetMeasuredCumulativePowerConsumption() {
            return (Getter)super.reqGetMeasuredCumulativePowerConsumption();
        }
        @Override
        public Getter reqGetManufacturersFaultCode() {
            return (Getter)super.reqGetManufacturersFaultCode();
        }
        @Override
        public Getter reqGetCurrentLimitSetting() {
            return (Getter)super.reqGetCurrentLimitSetting();
        }
        @Override
        public Getter reqGetFaultStatus() {
            return (Getter)super.reqGetFaultStatus();
        }
        @Override
        public Getter reqGetFaultDescription() {
            return (Getter)super.reqGetFaultDescription();
        }
        @Override
        public Getter reqGetManufacturerCode() {
            return (Getter)super.reqGetManufacturerCode();
        }
        @Override
        public Getter reqGetBusinessFacilityCode() {
            return (Getter)super.reqGetBusinessFacilityCode();
        }
        @Override
        public Getter reqGetProductCode() {
            return (Getter)super.reqGetProductCode();
        }
        @Override
        public Getter reqGetProductionNumber() {
            return (Getter)super.reqGetProductionNumber();
        }
        @Override
        public Getter reqGetProductionDate() {
            return (Getter)super.reqGetProductionDate();
        }
        @Override
        public Getter reqGetPowerSavingOperationSetting() {
            return (Getter)super.reqGetPowerSavingOperationSetting();
        }
        @Override
        public Getter reqGetRemoteControlSetting() {
            return (Getter)super.reqGetRemoteControlSetting();
        }
        @Override
        public Getter reqGetCurrentTimeSetting() {
            return (Getter)super.reqGetCurrentTimeSetting();
        }
        @Override
        public Getter reqGetCurrentDateSetting() {
            return (Getter)super.reqGetCurrentDateSetting();
        }
        @Override
        public Getter reqGetPowerLimitSetting() {
            return (Getter)super.reqGetPowerLimitSetting();
        }
        @Override
        public Getter reqGetCumulativeOperatingTime() {
            return (Getter)super.reqGetCumulativeOperatingTime();
        }
        @Override
        public Getter reqGetStatusChangeAnnouncementPropertyMap() {
            return (Getter)super.reqGetStatusChangeAnnouncementPropertyMap();
        }
        @Override
        public Getter reqGetSetPropertyMap() {
            return (Getter)super.reqGetSetPropertyMap();
        }
        @Override
        public Getter reqGetGetPropertyMap() {
            return (Getter)super.reqGetGetPropertyMap();
        }

        /**
         * Property name : Measured temperature value<br>
         * <br>
         * EPC : 0xE0<br>
         * <br>
         * Contents of property :<br>
         * This property indicates the measured temperature value in units of 0.1.C.<br>
         * <br>
         * Value range (decimal notation) :<br>
         * 0xF554.0x7FFE (-2732.32766) (-273.2.3276.6.C)<br>
         * <br>
         * Data type : signed short<br>
         * <br>
         * Data size : 2 bytes<br>
         * <br>
         * Unit : 0.1.C<br>
         * <br>
         * Access rule :<br>
         * Announce - undefined<br>
         * Set - undefined<br>
         * Get - mandatory<br>
         */
        public Getter reqGetMeasuredAirTemperatureValue() {
            reqGetProperty(EPC_AIR_TEMPERATURE_VALUE);
            return this;
        }
        public Getter reqGetMeasuredAirHumidityValue() {
            reqGetProperty(EPC_AIR_HUMIDITY_VALUE);
            return this;
        }
        public Getter reqGetMeasuredSoilMoistureValue() {
            reqGetProperty(EPC_SOIL_MOISTURE_VALUE);
            return this;
        }
        public Getter reqGetMeasuredSoilTemperatureValue() {
            reqGetProperty(EPC_SOIL_TEMPERATURE_VALUE);
            return this;
        }
        public Getter reqGetMeasuredLightLevelValue() {
            reqGetProperty(EPC_LIGHT_LEVEL_VALUE);
            return this;
        }
    }

    public static class Informer extends DeviceObject.Informer {
        public Informer(short echoClassCode, byte echoInstanceCode
                , String dstEchoAddress, boolean isSelfObject) {
            super(echoClassCode, echoInstanceCode
                    , dstEchoAddress, isSelfObject);
        }

        @Override
        public Informer reqInformProperty(byte epc) {
            return (Informer)super.reqInformProperty(epc);
        }
        @Override
        public Informer reqInformOperationStatus() {
            return (Informer)super.reqInformOperationStatus();
        }
        @Override
        public Informer reqInformInstallationLocation() {
            return (Informer)super.reqInformInstallationLocation();
        }
        @Override
        public Informer reqInformStandardVersionInformation() {
            return (Informer)super.reqInformStandardVersionInformation();
        }
        @Override
        public Informer reqInformIdentificationNumber() {
            return (Informer)super.reqInformIdentificationNumber();
        }
        @Override
        public Informer reqInformMeasuredInstantaneousPowerConsumption() {
            return (Informer)super.reqInformMeasuredInstantaneousPowerConsumption();
        }
        @Override
        public Informer reqInformMeasuredCumulativePowerConsumption() {
            return (Informer)super.reqInformMeasuredCumulativePowerConsumption();
        }
        @Override
        public Informer reqInformManufacturersFaultCode() {
            return (Informer)super.reqInformManufacturersFaultCode();
        }
        @Override
        public Informer reqInformCurrentLimitSetting() {
            return (Informer)super.reqInformCurrentLimitSetting();
        }
        @Override
        public Informer reqInformFaultStatus() {
            return (Informer)super.reqInformFaultStatus();
        }
        @Override
        public Informer reqInformFaultDescription() {
            return (Informer)super.reqInformFaultDescription();
        }
        @Override
        public Informer reqInformManufacturerCode() {
            return (Informer)super.reqInformManufacturerCode();
        }
        @Override
        public Informer reqInformBusinessFacilityCode() {
            return (Informer)super.reqInformBusinessFacilityCode();
        }
        @Override
        public Informer reqInformProductCode() {
            return (Informer)super.reqInformProductCode();
        }
        @Override
        public Informer reqInformProductionNumber() {
            return (Informer)super.reqInformProductionNumber();
        }
        @Override
        public Informer reqInformProductionDate() {
            return (Informer)super.reqInformProductionDate();
        }
        @Override
        public Informer reqInformPowerSavingOperationSetting() {
            return (Informer)super.reqInformPowerSavingOperationSetting();
        }
        @Override
        public Informer reqInformRemoteControlSetting() {
            return (Informer)super.reqInformRemoteControlSetting();
        }
        @Override
        public Informer reqInformCurrentTimeSetting() {
            return (Informer)super.reqInformCurrentTimeSetting();
        }
        @Override
        public Informer reqInformCurrentDateSetting() {
            return (Informer)super.reqInformCurrentDateSetting();
        }
        @Override
        public Informer reqInformPowerLimitSetting() {
            return (Informer)super.reqInformPowerLimitSetting();
        }
        @Override
        public Informer reqInformCumulativeOperatingTime() {
            return (Informer)super.reqInformCumulativeOperatingTime();
        }
        @Override
        public Informer reqInformStatusChangeAnnouncementPropertyMap() {
            return (Informer)super.reqInformStatusChangeAnnouncementPropertyMap();
        }
        @Override
        public Informer reqInformSetPropertyMap() {
            return (Informer)super.reqInformSetPropertyMap();
        }
        @Override
        public Informer reqInformGetPropertyMap() {
            return (Informer)super.reqInformGetPropertyMap();
        }

        /**
         * Property name : Measured temperature value<br>
         * <br>
         * EPC : 0xE0<br>
         * <br>
         * Contents of property :<br>
         * This property indicates the measured temperature value in units of 0.1.C.<br>
         * <br>
         * Value range (decimal notation) :<br>
         * 0xF554.0x7FFE (-2732.32766) (-273.2.3276.6.C)<br>
         * <br>
         * Data type : signed short<br>
         * <br>
         * Data size : 2 bytes<br>
         * <br>
         * Unit : 0.1.C<br>
         * <br>
         * Access rule :<br>
         * Announce - undefined<br>
         * Set - undefined<br>
         * Get - mandatory<br>
         */

        public Informer reqGetMeasuredAirTemperatureValue() {
            reqInformProperty(EPC_AIR_TEMPERATURE_VALUE);
            return this;
        }
        public Informer reqGetMeasureAirHumidityValue() {
            reqInformProperty(EPC_AIR_HUMIDITY_VALUE);
            return this;
        }
        public Informer reqGetMeasuredSoilMoistureValue() {
            reqInformProperty(EPC_SOIL_MOISTURE_VALUE);
            return this;
        }
        public Informer reqGetMeasuredAirHumidityValue() {
            reqInformProperty(EPC_AIR_HUMIDITY_VALUE);
            return this;
        }
        public Informer reqGetMeasuredLightLevelValue() {
            reqInformProperty(EPC_LIGHT_LEVEL_VALUE);
            return this;
        }
    }

    public static class Proxy extends AgricultureSensor {
        public Proxy(byte instanceCode) {
            super();
            mEchoInstanceCode = instanceCode;
        }
        @Override
        public byte getInstanceCode() {
            return mEchoInstanceCode;
        }
        @Override
        protected byte[] getOperationStatus() {return null;}

        @Override
        protected byte[] getAirTemperature() {
            return null;
        }

        @Override
        protected byte[] getSoilTemperature() {
            return null;
        }

        @Override
        protected byte[] getAirHumidity() {
            return null;
        }

        @Override
        protected byte[] getSoilMoisture() {
            return null;
        }

        @Override
        protected byte[] getLightLevel() {
            return null;
        }

        @Override
        protected boolean setInstallationLocation(byte[] edt) {return false;}
        @Override
        protected byte[] getInstallationLocation() {return null;}
        @Override
        protected byte[] getStandardVersionInformation() {return null;}
        @Override
        protected byte[] getFaultStatus() {return null;}
        @Override
        protected byte[] getManufacturerCode() {return null;}

    }

    public static Setter setG() {
        return setG((byte)0);
    }

    public static Setter setG(byte instanceCode) {
        return setG(instanceCode, true);
    }

    public static Setter setG(boolean responseRequired) {
        return setG((byte)0, responseRequired);
    }

    public static Setter setG(byte instanceCode, boolean responseRequired) {
        return new Setter(ECHO_CLASS_CODE, instanceCode
                , EchoSocket.MULTICAST_ADDRESS, responseRequired);
    }

    public static Getter getG() {
        return getG((byte)0);
    }

    public static Getter getG(byte instanceCode) {
        return new Getter(ECHO_CLASS_CODE, instanceCode
                , EchoSocket.MULTICAST_ADDRESS);
    }

    public static Informer informG() {
        return informG((byte)0);
    }

    public static Informer informG(byte instanceCode) {
        return new Informer(ECHO_CLASS_CODE, instanceCode
                , EchoSocket.MULTICAST_ADDRESS, false);
    }
}
