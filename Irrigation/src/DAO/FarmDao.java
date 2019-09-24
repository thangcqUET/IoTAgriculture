package DAO;

import model.Device;
import model.Farm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FarmDao implements Dao<Farm> {

    @Override
    public List<Farm> getAll() {
        Statement statement= null;
        List<Farm> farms = new ArrayList<Farm>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Farms";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Farm farm = new Farm(resultSet.getInt("FarmID"),
                        resultSet.getInt("Locate"),
                        resultSet.getDouble("Area"),
                        resultSet.getInt("FarmTypeID"),
                        resultSet.getInt("UserID")
                );
                farms.add(farm);
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
        return farms;
    }

    @Override
    public Farm getById(int id) {
        Statement statement;
        Farm farm = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Farms where FarmID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                farm = new Farm(resultSet.getInt("FarmID"),
                        resultSet.getInt("Locate"),
                        resultSet.getDouble("Area"),
                        resultSet.getInt("FarmTypeID"),
                        resultSet.getInt("UserID")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farm;
    }

    @Override
    public int save(Farm farm) {
        Statement statement;
        int farmId=0;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Farms(Locate,Area,FarmTypeID,UserID) values " +
                    "('"+farm.getLocate()+"',"+farm.getArea()+","+farm.getfarmTypeID()+","+farm.getUserID()+")";
            statement.execute(sql,Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            while(resultSet.next()){
                farmId=resultSet.getInt(1);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmId;
    }

    @Override
    public void update(Farm t_old, Farm t_new) {

    }

    @Override
    public void delete(long id) {

    }
}