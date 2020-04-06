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
                        resultSet.getInt("LocateID"),
                        resultSet.getDouble("Area"),
                        resultSet.getInt("FarmTypeID"),
                        resultSet.getBoolean("Status"),
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
                        resultSet.getInt("LocateID"),
                        resultSet.getDouble("Area"),
                        resultSet.getInt("FarmTypeID"),
                        resultSet.getBoolean("Status"),
                        resultSet.getInt("UserID")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farm;
    }
    public Integer getLocateIdById(int id) {
        Statement statement;
        Integer locateId = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select LocateID from Farms where FarmID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                locateId = resultSet.getInt("LocateID");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locateId;
    }

    @Override
    public int save(Farm farm) {
        Statement statement;
        int farmId=0;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Farms(LocateID,Area,FarmTypeID,Status,UserID) values " +
                    "("+farm.getLocateId()+","+farm.getArea()+","+farm.getFarmTypeID()+","+farm.getStatus()+","+farm.getUserID()+")";
            statement.execute(sql,Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            while(resultSet.next()){
                farmId=resultSet.getInt(1);
            }
            sql = "insert into Plots(Area, PlotTypeID, FarmID) values (null,null,"+farmId+");";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmId;
    }

    @Override
    public void update(Farm t_old, Farm t_new) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql1 = "update Farms set "
                    + ((t_old.getLocateId().equals(t_new.getLocateId()))?"":("LocateID = "+(t_new.getLocateId())+","))
                    + ((t_old.getArea().equals(t_new.getArea()))?"":("Area = "+(t_new.getArea())+","))
                    + ((t_old.getFarmTypeID().equals(t_new.getFarmTypeID()))?"":("FarmTypeID = "+(t_new.getFarmTypeID())+","))
                    + ((t_old.getStatus().equals(t_new.getStatus()))?"":("Status = "+(t_new.getStatus())+","))
                    + ((t_old.getUserID().equals(t_new.getUserID()))?"":("UserID = "+(t_new.getUserID())+","));
            String sql2 = " where FarmID = "+(t_old.getFarmID());
            String sql = sql1.substring(0,sql1.length()-1)+sql2;
//            System.out.println(sql);
            // tranh truong hop khong co noi dung update "update Farms set where FarmID = 4"
            if(!sql1.equals("update Farms set ")) statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "delete from Farms where FarmID = "+id;
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}