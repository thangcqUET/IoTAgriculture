package test.components.controller;

public class LoopController extends Controller {
    public int timeLoop;

    public LoopController(long deviceId, int timeLoop) {
        super(deviceId);
        this.timeLoop = timeLoop;
    }

    public LoopController(long deviceId) {
        super(deviceId);
        this.timeLoop = 30000;
    }

    public void start(){
        Boolean currentStatus = false;
        while(true) {
            try {
                Thread.sleep(timeLoop);
                setAmountOfWater(5F);
                //currentStatus = !currentStatus;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
