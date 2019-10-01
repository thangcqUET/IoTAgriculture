package DAO;

import model.Device;
import model.DeviceType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeviceTypeDao implements Dao<DeviceType>{

    @Override
    public List<DeviceType> getAll() {
        Statement statement= null;
        List<DeviceType> deviceTypes = new ArrayList<DeviceType>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from DeviceTypes";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                DeviceType deviceType = new DeviceType(resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceType")
                );
                deviceTypes.add(deviceType);
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
        return deviceTypes;
    }

    @Override
    public DeviceType getById(int id) {
        Statement statement;
        DeviceType deviceType = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from DeviceTypes where DeviceTypeID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                deviceType = new DeviceType(resultSet.getInt("DeviceTypeID"),
                        resultSet.getString("DeviceType")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deviceType;
    }

    @Override
    public int save(DeviceType deviceType) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into DeviceTypes(DeviceTypeID, DeviceType) values " +
                    "("+deviceType.getDeviceTypeId()+",'"+deviceType.getDeviceType()+"')";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(DeviceType t_old, DeviceType t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
