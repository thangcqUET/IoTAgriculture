package DAO;

import model.FarmType;
import model.Locate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocateDao implements Dao<Locate> {
    @Override
    public List<Locate> getAll() {
        Statement statement= null;
        List<Locate> locates = new ArrayList<Locate>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Locates";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Locate locate = new Locate(resultSet.getInt("LocateID"),
                        resultSet.getString("Locate"),
                        resultSet.getFloat("Lon"),
                        resultSet.getFloat("Lat")
                );
                locates.add(locate);
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
        return locates;
    }

    @Override
    public Locate getById(int id) {
        Statement statement;
        Locate locate = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Locates where LocateID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                locate = new Locate(resultSet.getInt("LocateID"),
                        resultSet.getString("Locate"),
                        resultSet.getFloat("Lon"),
                        resultSet.getFloat("Lat")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locate;
    }

    @Override
    public int save(Locate locate) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Locates(LocateID, LocateName, Lon, Lat) values " +
                    "("+locate.getLocateId()+",'"+locate.getLocate()+"',"+locate.getLon()+","+locate.getLat()+")";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Locate t_old, Locate t_new) {

    }

    @Override
    public void delete(long id) {

    }
}