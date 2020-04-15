package components;

import DAO.LocateDao;
import DAO.WeatherForecastDao;
import model.Locate;
import model.WeatherForecast;

import java.time.LocalTime;
import java.util.ArrayList;

public class WeatherForecastCollector implements Runnable {
    @Override
    public void run() {
        boolean updated=false;
        while(true) {
            LocalTime time = LocalTime.now();
            if(time.getMinute()==00) {
                if(!updated) {
                    System.out.println("WeatherForecastCollector 19");
                    System.out.println("weather forecast is updating...");
                    WeatherForecastDao weatherForecastDao = new WeatherForecastDao();
                    LocateDao locateDao = new LocateDao();
                    ArrayList<Locate> locates = (ArrayList<Locate>) locateDao.getAll();
                    for(Locate l:locates){
                        WeatherForecast weatherForecast = new WeatherForecast(l.getLocateId());
                        weatherForecast.update();
                        weatherForecastDao.save(weatherForecast);
                    }
                    updated = true;
                }
            }else {
                updated=false;
            }
        }
    }
}
