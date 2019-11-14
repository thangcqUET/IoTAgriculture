package components.controller;

import Utilities.Helper;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class CycleController extends Controller {
    Integer wateringInterval; //s
    Timestamp timeOfWatering;
    Integer cycle;
    public CycleController(Long deviceId, Integer wateringInterval) {
        super(deviceId);
        this.wateringInterval = wateringInterval;
    }

    public CycleController(Long deviceId) {
        super(deviceId);
        this.wateringInterval = 120000;
    }

    @Override
    public void start() {
        long period = 24*60*60*1000;
        Calendar morning = Calendar.getInstance();
        morning.set(Calendar.HOUR_OF_DAY, 21);
        morning.set(Calendar.MINUTE, 45);
        morning.set(Calendar.SECOND, 0);
        morning.set(Calendar.MILLISECOND, 0);
        Calendar afternoon = Calendar.getInstance();
        afternoon.set(Calendar.HOUR_OF_DAY, 21);
        afternoon.set(Calendar.MINUTE, 40);
        afternoon.set(Calendar.SECOND, 0);
        afternoon.set(Calendar.MILLISECOND, 0);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setStatus(true);
                try {
                    Thread.sleep(wateringInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setStatus(false);
            }
        };
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        timer1.schedule(timerTask,morning.getTime(),period);
    }
}
