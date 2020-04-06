import DAO.FarmDao;
import DAO.PlotDao;
import DAO.UserDao;
import Utilities.Helper;
import components.ControllingDataSender;
import components.DataCollector;
import components.autoController.MPC;
import model.Farm;
import model.Plot;
import model.User;

import java.sql.Timestamp;
import java.util.Calendar;

public class MainClass {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        FarmDao farmDao = new FarmDao();
        if(userDao.getByUsernameAndPassword("admin","12345678")==null){
            userDao.save(new User("admin",Helper.md5("12345678")));
        }
        if(farmDao.getById(1)==null){
            farmDao.save(new Farm(null,null,null,null,1));
        }


//        Thread dataCollectorThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DataCollector dataCollector = new DataCollector();
//            }
//        });
//        Thread mpcThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//        //ControllingDataSender controllingDataSender = new ControllingDataSender();
//                MPC mpc = new MPC();
//                mpc.process();
//            }
//        });
//        dataCollectorThread.start();
//        mpcThread.start();

        DataCollector dataCollector = new DataCollector();
        MPC mpc = new MPC();
        mpc.process();
    }
}
