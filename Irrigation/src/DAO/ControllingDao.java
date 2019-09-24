package DAO;

import model.Controlling;
import model.Forecast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ControllingDao implements Dao<Controlling>{

    @Override
    public List<Controlling> getAll() {
        Statement statement;
        List<Controlling> controllings = new ArrayList<Controlling>();
        try {
            Controlling controlling = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Controlling";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                controlling = new Controlling(resultSet.getLong("ControllingID"),
                        resultSet.getLong("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getFloat("AmountOfWater"),
                        resultSet.getInt("WateringDuration"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
                controllings.add(controlling);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controllings;
    }

    @Override
    public Controlling getById(int id) {
        Statement statement;
        Controlling controlling = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Controlling where ControllingID = "+id;
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                controlling = new Controlling(resultSet.getLong("ControllingID"),
                        resultSet.getLong("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getFloat("AmountOfWater"),
                        resultSet.getInt("WateringDuration"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controlling;
    }

    @Override
    public int save(Controlling controlling) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Controlling (DeviceID, PlotID, AmountOfWater, WateringDuration, TimeOfControl) values " +
                    "("+controlling.getDeviceID()+","+controlling.getPlotID()+","+controlling.getAmountOfWater()+","+controlling.getWateringDuration()+","+controlling.getTimeOfControl()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Controlling t_old, Controlling t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
