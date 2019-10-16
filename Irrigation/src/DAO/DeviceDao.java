package DAO;

import model.Device;
import model.Farm;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeviceDao implements Dao<Device> {
    @Override
    public List<Device> getAll() {
        Statement statement= null;
        List<Device> devices = new ArrayList<Device>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Devices";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Device device = new Device(resultSet.getLong("DeviceID"),
                        resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceName"),
                        resultSet.getBoolean("Status"),
                        resultSet.getInt("PlotID")
                );
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return devices;
    }

    public List<Device> getByFarmId(Integer farmId){
        Statement statement= null;
        List<Device> devices = new ArrayList<Device>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Devices where PlotID in (select PlotID from Plots where FarmID = "+farmId+");";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Device device = new Device(resultSet.getLong("DeviceID"),
                        resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceName"),
                        resultSet.getBoolean("Status"),
                        resultSet.getInt("PlotID")
                );
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return devices;
    }
    @Override
    public Device getById(int id) {
        Statement statement;
        Device device = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Devices where DeviceID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                device = new Device(resultSet.getLong("DeviceID"),
                        resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceName"),
                        resultSet.getBoolean("Status"),
                        resultSet.getInt("PlotID")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return device;
    }

    public Device getById(Long id) {
        Statement statement;
        Device device = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Devices where DeviceID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                device = new Device(resultSet.getLong("DeviceID"),
                        resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceName"),
                        resultSet.getBoolean("Status"),
                        resultSet.getInt("PlotID")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return device;
    }

    @Override
    public int save(Device device) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Devices(DeviceID, DeviceTypeID,DeviceName,Status,PlotID) values " +
                    "("+device.getDeviceID()+","+device.getDeviceTypeID()+",'"+device.getDeviceName()+"',"+device.getStatus()+","+device.getPlotID()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Device t_old, Device t_new) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql1 = "update Devices set "
                    + ((t_old.getDeviceID().equals(t_new.getDeviceID()))?"":("LocateID = "+(t_new.getDeviceID())+","))
                    + ((t_old.getDeviceTypeID().equals(t_new.getDeviceTypeID()))?"":("Area = "+(t_new.getDeviceTypeID())+","))
                    + ((t_old.getDeviceName().equals(t_new.getDeviceName()))?"":("FarmTypeID = "+(t_new.getDeviceName())+","))
                    + ((t_old.getStatus().equals(t_new.getStatus()))?"":("Status = "+(t_new.getStatus())+","))
                    + ((t_old.getPlotID().equals(t_new.getPlotID()))?"":("UserID = "+(t_new.getPlotID())+","));
            String sql2 = " where DeviceID = "+(t_old.getDeviceID());
            String sql = sql1.substring(0,sql1.length()-1)+sql2;
//            System.out.println(sql);
            // tranh truong hop khong co noi dung update "update Farms set where FarmID = 4"
            if(!sql1.equals("update Devices set ")) statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {

    }
}
