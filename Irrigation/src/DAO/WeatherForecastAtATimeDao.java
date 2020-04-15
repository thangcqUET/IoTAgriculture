package DAO;

import model.WeatherForecast;
import model.WeatherForecastAtATime;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecastAtATimeDao implements Dao<WeatherForecastAtATime> {
    @Override
    public List<WeatherForecastAtATime> getAll() {
        Statement statement;
        List<WeatherForecastAtATime> weatherForecastAtATimes = new ArrayList<WeatherForecastAtATime>();
        try {
            WeatherForecastAtATime weatherForecastAtATime = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from WeatherForecastAtATimes";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                weatherForecastAtATime = new WeatherForecastAtATime(
                        resultSet.getInt("WeatherForecastID"),
                        new Date(resultSet.getTime("ForecastTime").getTime()),
                        resultSet.getLong("EpochTime"),
                        resultSet.getString("ForecastStatus"),
                        resultSet.getBoolean("IsDayLight"),
                        resultSet.getFloat("Temperature"),
                        resultSet.getFloat("WindSpeed"),
                        resultSet.getFloat("RelativeHumidity"),
                        resultSet.getByte("RainProbability"),
                        resultSet.getByte("PrecipitationProbability"),
                        resultSet.getFloat("RainValue"),
                        resultSet.getByte("CloudCover")
                );
                weatherForecastAtATimes.add(weatherForecastAtATime);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherForecastAtATimes;
    }

    @Override
    public WeatherForecastAtATime getById(int id) {
        return null;
    }


    public List<WeatherForecastAtATime> getByWeatherForecastId(int id) {
        Statement statement;
        List<WeatherForecastAtATime> weatherForecastAtATimes = new ArrayList<WeatherForecastAtATime>();
        try {
            WeatherForecastAtATime weatherForecastAtATime = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from WeatherForecastAtATimes where WeatherForecastID = "+id;
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                weatherForecastAtATime = new WeatherForecastAtATime(
                        resultSet.getInt("WeatherForecastID"),
                        new Date(resultSet.getTime("ForecastTime").getTime()),
                        resultSet.getLong("EpochTime"),
                        resultSet.getString("ForecastStatus"),
                        resultSet.getBoolean("IsDayLight"),
                        resultSet.getFloat("Temperature"),
                        resultSet.getFloat("WindSpeed"),
                        resultSet.getFloat("RelativeHumidity"),
                        resultSet.getByte("RainProbability"),
                        resultSet.getByte("PrecipitationProbability"),
                        resultSet.getFloat("RainValue"),
                        resultSet.getByte("CloudCover")
                );
                weatherForecastAtATimes.add(weatherForecastAtATime);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherForecastAtATimes;
    }



    @Override
    public int save(WeatherForecastAtATime weatherForecastAtATime) {
        try {
            String sql = "insert into WeatherForecastAtATimes (" +
                    "WeatherForecastID, " +
                    "ForecastTime, " +
                    "EpochTime, " +
                    "ForecastStatus, " +
                    "IsDayLight, " +
                    "Temperature, " +
                    "WindSpeed, " +
                    "RelativeHumidity, " +
                    "RainProbability, " +
                    "PrecipitationProbability, " +
                    "RainValue, " +
                    "CloudCover) " +
                    "values " +
                    "("+weatherForecastAtATime.getWeatherForecastId()+","+
                    "?,"+
                    weatherForecastAtATime.getEpochDataTime()+","+
                    "'"+weatherForecastAtATime.getForecastStatus()+"'"+","+
                    weatherForecastAtATime.getDaylight()+","+
                    weatherForecastAtATime.getTemperature()+","+
                    weatherForecastAtATime.getWindSpeed()+","+
                    weatherForecastAtATime.getRelativeHumidity()+","+
                    weatherForecastAtATime.getRainProbability()+","+
                    weatherForecastAtATime.getPrecipitationProbability()+","+
                    weatherForecastAtATime.getRainValue()+","+
                    weatherForecastAtATime.getCloudCover()
                    +")";
            PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            Timestamp forecastTime = new Timestamp(weatherForecastAtATime.getDateTime().getTime());
            preparedStatement.setTimestamp(1,forecastTime);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(WeatherForecastAtATime t_old, WeatherForecastAtATime t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
