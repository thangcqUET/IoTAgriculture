package components.controller;

public class LoopController extends Controller {
    public int timeLoop;
    Float amountOfWater;
    public LoopController(long deviceId, int timeLoop) {
        super(deviceId);
        this.timeLoop = timeLoop;
    }

    public LoopController(long deviceId) {
        super(deviceId);
        this.timeLoop = 60000;
    }

    protected void setAmountOfWater(Float amountOfWater){
        this.amountOfWater=amountOfWater;
        onControlListener.onReceivedControllingData(amountOfWater);
    }
    public void start(){
        Boolean currentStatus = false;
        while(true) {
            try {
                Thread.sleep(timeLoop);
                setAmountOfWater(3F);
                //currentStatus = !currentStatus;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
