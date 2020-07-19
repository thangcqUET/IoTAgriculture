import DAO.FarmDao;
import DAO.PlotDao;
import DAO.UserDao;
import Utilities.Helper;
import components.ControllingDataSender;
import components.DataCollector;
import components.WeatherForecastCollector;
import components.autoController.MPC;
import model.Farm;
import model.Plot;
import model.User;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Calendar;

public class MainClass {
    public static void main(String[] args) {
        System.out.println("Version: "+1);
        UserDao userDao = new UserDao();
        FarmDao farmDao = new FarmDao();
        if(userDao.getByUsernameAndPassword("admin","12345678")==null){
            userDao.save(new User("admin",Helper.md5("12345678")));
        }
        if(farmDao.getById(1)==null){
            farmDao.save(new Farm(null,null,null,null,1));
        }


        Thread dataCollectorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataCollector dataCollector = new DataCollector();
                dataCollector.run();
                boolean isPrint = false;
                while(true){
                    LocalTime now = LocalTime.now();
                    if(now.getSecond()%120==0) {
                        if(!isPrint) System.out.println("Data collector is running...");
                        isPrint=true;
                    }else{
                        isPrint=false;
                    }
                }
            }
        });
        Thread controlThread = new Thread(new Runnable() {
            @Override
            public void run() {
//                ControllingDataSender controllingDataSender = new ControllingDataSender();
                MPC mpc = new MPC();
                mpc.process();
            }
        });

        WeatherForecastCollector weatherForecastCollector = new WeatherForecastCollector();
        Thread weatherForecastCollectorThread = new Thread(weatherForecastCollector);

        dataCollectorThread.start();
        controlThread.start();
        weatherForecastCollectorThread.start();

//        DataCollector dataCollector = new DataCollector();
//        dataCollector.run();
//        MPC mpc = new MPC();
//        mpc.process();
    }
}
