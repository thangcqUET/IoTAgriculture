package DAO;

import model.Sensing;

import java.sql.*;
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
            Long deviceId;
            Integer plotId, lightLevel;
            Float soilMoisture, soilTemperature, humidity, temperature;
            Timestamp timeOfMeasurement;
            deviceId = sensing.getDeviceID();
            plotId = sensing.getPlotID();
            soilMoisture = sensing.getSoilMoisture();
            soilTemperature = sensing.getSoilTemperature();
            humidity = sensing.getHumidity();
            temperature = sensing.getTemperature();
            lightLevel = sensing.getLightLevel();
            timeOfMeasurement = sensing.getTimeOfMeasurement();
            String sql = "insert into Sensing (DeviceID, PlotID, SoilMoisture, SoilTemperature, Humidity,LightLevel,Temperature, TimeOfMeasurement) " +
                    "values ("+deviceId+","+plotId+","+soilMoisture+","+soilTemperature+","+humidity+","+lightLevel+","+temperature+",?)";
            PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(sql);
            preparedStatement.setTimestamp(1,timeOfMeasurement);
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
