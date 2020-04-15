package components.autoController;

import DAO.LocateDao;
import DAO.WeatherForecastDao;
import model.Forecast;
import model.Locate;
import model.WeatherForecast;

import java.time.LocalTime;
import java.util.ArrayList;

public class WeatherForecasts {
    private ArrayList<WeatherForecast> weatherForecastArrayList;
    private static WeatherForecasts weatherForecasts=null;

    private WeatherForecasts(){
        weatherForecastArrayList = new ArrayList<WeatherForecast>();
    }

    public static WeatherForecasts getInstance(){
        if(weatherForecasts==null){
            weatherForecasts = new WeatherForecasts();
        }
        return weatherForecasts;
    }
    void update(String locateId){
        //http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/353412?apikey=LNrGryrjcdTBVNpPvJLCPWdg0lcZCQ4D&language=en-us&details=true&metric=true

    }

    public ArrayList<WeatherForecast> getForecasts() {
        return weatherForecastArrayList;
    }

    WeatherForecast getWeatherForecastByLocateId(String locateId){
        WeatherForecastDao weatherForecastDao = new WeatherForecastDao();
        WeatherForecast weatherForecast = weatherForecastDao.getLatestByLocateId(locateId);
        if(weatherForecast==null){
            LocateDao locateDao = new LocateDao();
            locateDao.save(new Locate(locateId,"New Location"));
            weatherForecast = new WeatherForecast(locateId);
            weatherForecast.update();
            weatherForecastDao.save(weatherForecast);
            return weatherForecast;
        }else{
            return weatherForecast;
        }
    }

}
