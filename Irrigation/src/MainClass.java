import DAO.FarmDao;
import DAO.PlotDao;
import DAO.UserDao;
import Utilities.Helper;
import components.ControllingDataSender;
import components.DataCollector;
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
        DataCollector dataCollector = new DataCollector();
        ControllingDataSender controllingDataSender = new ControllingDataSender();
    }
}
