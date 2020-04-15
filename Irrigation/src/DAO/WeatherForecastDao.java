package DAO;

import model.WeatherForecast;
import model.Sensing;
import model.WeatherForecastAtATime;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecastDao implements Dao<WeatherForecast> {
    @Override
    public List<WeatherForecast> getAll() {
        Statement statement;
        List<WeatherForecast> weatherForecasts = new ArrayList<WeatherForecast>();
        try {
            WeatherForecast weatherForecast = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from WeatherForecasts";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                weatherForecast = new WeatherForecast(resultSet.getInt("WeatherForecastID"),
                        resultSet.getString("LocateID"),
                        resultSet.getTime("CurrentTime").toLocalTime()
                );
                weatherForecasts.add(weatherForecast);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherForecasts;
    }

    @Override
    public WeatherForecast getById(int id) {
        Statement statement;
        WeatherForecast weatherForecast = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from WeatherForecasts where WeatherForecastID = "+id;
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                weatherForecast = new WeatherForecast(resultSet.getInt("WeatherForecastID"),
                        resultSet.getString("LocateID"),
                        resultSet.getTime("CurrentTime").toLocalTime()
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherForecast;
    }

    public List<WeatherForecast> getByLocateId(String locateId){
        Statement statement;
        List<WeatherForecast> weatherForecasts = new ArrayList<WeatherForecast>();
        try {
            WeatherForecast weatherForecast = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from WeatherForecasts where LocateID = '"+locateId+"';";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                int weatherForercastId = resultSet.getInt("WeatherForecastID");
                weatherForecast = new WeatherForecast(weatherForercastId,
                        resultSet.getString("LocateID"),
                        resultSet.getTime("CurrentTime").toLocalTime()
                );
                WeatherForecastAtATimeDao weatherForecastAtATimeDao = new WeatherForecastAtATimeDao();
                ArrayList<WeatherForecastAtATime> weatherForecastAtATimes =
                        (ArrayList<WeatherForecastAtATime>) weatherForecastAtATimeDao.getByWeatherForecastId(weatherForercastId);
                weatherForecast.setWeatherForecastAtATimes(weatherForecastAtATimes);
                weatherForecasts.add(weatherForecast);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherForecasts;
    }

    public WeatherForecast getLatestByLocateId(String locateId){
        Statement statement;
        List<WeatherForecast> weatherForecasts = new ArrayList<WeatherForecast>();
        try {
            WeatherForecast weatherForecast = null;
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from WeatherForecasts where LocateID = '"+locateId+"' order by WeatherForecastID desc limit 1;";
            ResultSet resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                int weatherForercastId = resultSet.getInt("WeatherForecastID");
                weatherForecast = new WeatherForecast(weatherForercastId,
                        resultSet.getString("LocateID"),
                        resultSet.getTime("CurrentTime").toLocalTime()
                );
                WeatherForecastAtATimeDao weatherForecastAtATimeDao = new WeatherForecastAtATimeDao();
                ArrayList<WeatherForecastAtATime> weatherForecastAtATimes =
                        (ArrayList<WeatherForecastAtATime>) weatherForecastAtATimeDao.getByWeatherForecastId(weatherForercastId);
                weatherForecast.setWeatherForecastAtATimes(weatherForecastAtATimes);
                weatherForecasts.add(weatherForecast);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherForecasts.get(0);
    }

    @Override
    public int save(WeatherForecast weatherForecast) {
        int weatherForecastId=0;
        try {
            Timestamp currentTime = Timestamp.valueOf(weatherForecast.getCurTime().atDate(LocalDate.now()));
            System.out.println("WeatherForecastDao 116");
            System.out.println(currentTime);
//            currentTime = Timestamp.valueOf(LocalDateTime.now());
            String sql = "insert into WeatherForecasts (LocateID, CurrentTime) values " +
                    "('"+weatherForecast.getLocateId()+"', ?)";
            PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1,currentTime);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                weatherForecastId=resultSet.getInt(1);
            }
            preparedStatement.close();
            for(WeatherForecastAtATime wfat: weatherForecast.getWeatherForecastAtATimes()){
                wfat.setWeatherForecastId(weatherForecastId);
                WeatherForecastAtATimeDao weatherForecastAtATimeDao = new WeatherForecastAtATimeDao();
                weatherForecastAtATimeDao.save(wfat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(WeatherForecast t_old, WeatherForecast t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
