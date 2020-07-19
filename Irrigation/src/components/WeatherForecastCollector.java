package components;

import DAO.LocateDao;
import DAO.WeatherForecastDao;
import model.Locate;
import model.WeatherForecast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class WeatherForecastCollector implements Runnable {
    @Override
    public void run() {
        while(true) {
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


            try {
                LocalDateTime datetime = LocalDateTime.now();
                LocalDateTime nextDateTime = datetime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
                Thread.sleep((nextDateTime.toEpochSecond(ZoneOffset.UTC)-datetime.toEpochSecond(ZoneOffset.UTC))*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
