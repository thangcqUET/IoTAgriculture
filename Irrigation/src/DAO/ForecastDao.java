package DAO;

import model.Forecast;
import model.Sensing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ForecastDao implements Dao<Forecast> {
    @Override
    public List<Forecast> getAll() {
        Statement statement;
        List<Forecast> forecasts = new ArrayList<Forecast>();
        try {
            Forecast forecast = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Forecast";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                forecast = new Forecast(resultSet.getLong("ForecastID"),
                        resultSet.getLong("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getFloat("Precipitation"),
                        resultSet.getFloat("Humidity"),
                        resultSet.getFloat("Temperature"),
                        resultSet.getString("ForecastStatus"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
                forecasts.add(forecast);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return forecasts;
    }

    @Override
    public Forecast getById(int id) {
        Statement statement;
        Forecast forecast = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Forecast where ForecastID = "+id;
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                forecast = new Forecast(resultSet.getLong("ForecastID"),
                        resultSet.getLong("DeviceID"),
                        resultSet.getInt("PlotID"),
                        resultSet.getFloat("Precipitation"),
                        resultSet.getFloat("Humidity"),
                        resultSet.getFloat("Temperature"),
                        resultSet.getString("ForecastStatus"),
                        resultSet.getTimestamp("TimeOfMeasurement"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return forecast;
    }

    @Override
    public int save(Forecast forecast) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Forecast (DeviceID, PlotID, Precipitation,Temperature, ForecastStatus, TimeOfMeasurement) values " +
                    "("+forecast.getDeviceID()+","+forecast.getPlotID()+","+forecast.getTemperature()+","+forecast.getForecastStatus()+","+forecast.getTimeOfMeasurement()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Forecast t_old, Forecast t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
