package DAO;

import model.DeviceType;
import model.FarmType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FarmTypeDao implements Dao<FarmType>{
    @Override
    public List<FarmType> getAll() {
        Statement statement= null;
        List<FarmType> farmTypes = new ArrayList<FarmType>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from FarmTypes";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                FarmType farmType = new FarmType(resultSet.getInt("FarmTypeID"),
                        resultSet.getString("FarmType")
                );
                farmTypes.add(farmType);
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
        return farmTypes;
    }

    @Override
    public FarmType getById(int id) {
        Statement statement;
        FarmType farmType = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from FarmTypes where FarmTypeID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                farmType = new FarmType(resultSet.getInt("FarmTypeID"),
                        resultSet.getString("FarmType")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmType;
    }

    @Override
    public int save(FarmType farmType) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into FarmTypes(FarmTypeID, FarmType) values " +
                    "("+farmType.getFarmTypeId()+","+farmType.getFarmType()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(FarmType t_old, FarmType t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
