package utilites;

public class Timer {
    private static boolean canSend;
    private static int timeValue;
    public Timer(){
        canSend=true;
        timeValue=400;
    }
    public synchronized void waitToReceive(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    canSend=false;
                    Thread.sleep(timeValue);
                    canSend=true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    public boolean isCanSend() {
        return canSend;
    }
}
