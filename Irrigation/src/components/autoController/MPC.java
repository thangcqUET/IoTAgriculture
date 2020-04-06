package components.autoController;

import components.controller.Controller;
import model.Device;

import java.time.LocalTime;
import java.util.*;

public class MPC {
    private static DeviceControlUnitManager dcum;
    private static Optimizer optimizer;
    public MPC(){
        optimizer = new Optimizer();
        dcum = DeviceControlUnitManager.getInstance();
        System.out.println("DCUM: "+dcum);
    }
    /**
     * hàm này sẽ thực hiện những xử lý sau:
     * - lấy ra những DeviceControlUnit cần thực hiện và thực hiện chúng
     * - để tránh việc xử lý 1 DCU quá lâu khiến cho các DCU khác không được thực hiện vì quá thời gian, chương trình
     * sẽ lấy DCU ra trước rồi mới thực hiện
     * - hàm sẽ kiểm tra xem DCU có đang ở chế độ tưới tự động hay không. Nếu phải thì mới thực hiện. Nếu không thì loại
     * ra khỏi queue. Sau khi thực hiện thì lại add vào queue
     * - khi 1 DCU được chuyển sang chế độ tưới tự động, nó sẽ ngay lập tức đưa vào queue.(CHƯA THỰC HIỆN)
     */
    public void process() {
        System.out.println("processing....");
        boolean isPrint = false;
        while(true) {
            LocalTime now = LocalTime.now();
            if(now.getSecond()%30==0) {
                if(!isPrint) System.out.println(now);
                isPrint=true;
            }else{
                isPrint=false;
            }
            if(!dcum.isEmpty()) {
//                if((now.getMinute())==0){
//                    System.out.println(now.withNano(0).withSecond(0).compareTo(dcum.peek().getLatestIrrigationTime().plusHours(1).withNano(0).withSecond(0)));
//                }
                if(now.withNano(0).withSecond(0).compareTo(dcum.peek().getLatestIrrigationTime().plusHours(1).withNano(0).withSecond(0))==0){
                    System.out.println("NOW: "+now+" DCU TIME: "+dcum.peek().getLatestIrrigationTime());
                    ArrayList<DeviceControlUnit> dcus = new ArrayList<>();
                    // tranh TH xu ly lau hon 1 phut
                    while((!dcum.isEmpty())&&(now.withNano(0).withSecond(0).compareTo(dcum.peek().getLatestIrrigationTime().plusHours(1).withNano(0).withSecond(0))==0)){
                        System.out.println("REMOVE...");
                        dcus.add(dcum.remove());
                        System.out.println(dcus.size());
                    }
                    //System.out.println("exist loop...");
                    System.out.println(dcus.size());
                    for(DeviceControlUnit dcu: dcus){
                        if(dcu.needProcess()) {
                            System.out.println("OPTIMIZE");
                            System.out.println(dcu);
                            optimizer.process(dcu);
                            dcum.add(dcu);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MPC mpc = new MPC();
//        DeviceControlUnit dcu = new DeviceControlUnit();
//        dcum.add(dcu);
//        mpc.process();
        for(int i=0;i<2;i++){
            DeviceControlUnit dcu = new DeviceControlUnit();
            dcum.add(dcu);
            Thread.sleep(15000);
        }

        mpc.process();
        //optimizer.process(dcu);
//        LocalTime time1=LocalTime.of(23,0);
//        LocalTime time2=LocalTime.of(0,0);
//        System.out.println(time1.compareTo(time2));
    }
}
