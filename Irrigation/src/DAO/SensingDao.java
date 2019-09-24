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
                        resultSet.getLong("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getFloat("SoilMoisture"),
                        resultSet.getFloat("SoilTemperature"),
                        resultSet.getFloat("Humidity"),
                        resultSet.getFloat("Temperature"),
                        resultSet.getInt("LightLevel"),
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
                        resultSet.getLong("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getFloat("SoilMoisture"),
                        resultSet.getFloat("SoilTemperature"),
                        resultSet.getFloat("Humidity"),
                        resultSet.getFloat("Temperature"),
                        resultSet.getInt("LightLevel"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sensing;
    }

    @Override
    public int save(Sensing sensing) {

        try {
            String sql = "insert into Sensing (DeviceID, PlotID, SoilMoisture, SoilTemperature, Humidity,LightLevel,Temperature, TimeOfMeasurement) values (?,?,?,?,?,?,?,?)";
//                    +
//                    "("+sensing.getDiviceID()+","+sensing.getPlotID()+","+sensing.getSoilMoisture()+","+sensing.getHumidity()+","+sensing.getLightLevel()+","+sensing.getTemperature()+","+"CURRENT_TIMESTAMP"+")";
            PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(sql);
            preparedStatement.setLong(1,sensing.getDeviceID());
            preparedStatement.setInt(2,sensing.getPlotID());
            preparedStatement.setFloat(3,sensing.getSoilMoisture());
            preparedStatement.setFloat(4,sensing.getSoilTemperature());
            preparedStatement.setFloat(5,sensing.getHumidity());
            preparedStatement.setFloat(6,sensing.getTemperature());
            preparedStatement.setInt(7,sensing.getLightLevel());
            preparedStatement.setTimestamp(8,sensing.getTimeOfMeasurement());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Sensing t_old, Sensing t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
