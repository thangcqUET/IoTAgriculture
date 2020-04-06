package components.autoController;

import model.Forecast;
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
        boolean found = false;
        for(WeatherForecast weatherForecast:weatherForecastArrayList){
            if(weatherForecast.getLocateId().equals(locateId)){
                found = true;
                weatherForecast.updateTest();
                return weatherForecast;
            }
        }
        if(found == false){
            WeatherForecast weatherForecast = new WeatherForecast(locateId);
            if(weatherForecast!=null){
                weatherForecast.updateTest();
                weatherForecastArrayList.add(weatherForecast);
                return weatherForecast;
            }else{
                return null;
            }
        }
        return null;
    }

}
