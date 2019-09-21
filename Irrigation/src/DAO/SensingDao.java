package DAO;

import model.Sensing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SensingDao implements Dao<Sensing> {
    @Override
    public List<Sensing> getAll() {
        Statement statement;
        List<Sensing> sensings = new ArrayList<Sensing>();
        try {
            Sensing sensing = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Sensing";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                sensing = new Sensing(resultSet.getLong("SensingID"),
                        resultSet.getInt("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getByte("SoilMoisture"),
                        resultSet.getByte("Humidity"),
                        resultSet.getInt("LightLevel"),
                        resultSet.getByte("Temperature"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
                sensings.add(sensing);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sensings;
    }

    @Override
    public Sensing getById(int id) {
        Statement statement;
        Sensing sensing = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Sensing where SensingID = "+id;
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                sensing = new Sensing(resultSet.getLong("SensingID"),
                        resultSet.getInt("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getByte("SoilMoisture"),
                        resultSet.getByte("Humidity"),
                        resultSet.getInt("LightLevel"),
                        resultSet.getByte("Temperature"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sensing;
    }

    @Override
    public void save(Sensing sensing) {

        try {
            String sql = "insert into Sensing (DeviceID, PlotID, SoilMoisture, Humidity,LightLevel,Temperature, TimeOfMeasurement) values (?,?,?,?,?,?,?)";
//                    +
//                    "("+sensing.getDiviceID()+","+sensing.getPlotID()+","+sensing.getSoilMoisture()+","+sensing.getHumidity()+","+sensing.getLightLevel()+","+sensing.getTemperature()+","+"CURRENT_TIMESTAMP"+")";
            PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1,sensing.getDiviceID());
            preparedStatement.setInt(2,sensing.getPlotID());
            preparedStatement.setInt(3,sensing.getSoilMoisture());
            preparedStatement.setInt(4,sensing.getHumidity());
            preparedStatement.setInt(5,sensing.getLightLevel());
            preparedStatement.setInt(6,sensing.getTemperature());
            preparedStatement.setTimestamp(7,sensing.getTimeOfMeasurement());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Sensing t_old, Sensing t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
