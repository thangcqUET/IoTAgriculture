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
                Device device = new Device(resultSet.getInt("DeviceID"),
                        resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceName"),
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
                device = new Device(resultSet.getInt("DeviceID"),
                        resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceName"),
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
    public void save(Device device) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Devices(DeviceID, DeviceTypeID,DeviceName,PlotID) values " +
                    "("+device.getDeviceID()+","+device.getDeviceTypeID()+",'"+device.getDeviceName()+"',"+device.getPlotID()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Device t_old, Device t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
