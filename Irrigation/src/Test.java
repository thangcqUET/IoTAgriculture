import Utilities.Helper;
import components.autoController.DeviceControlUnit;
import model.Forecast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    public static void main(String[] args) throws ParseException, InterruptedException, IOException {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        long a = Long.parseLong(processName.split("@")[0]);
        System.out.println(a);
        while(true){
            Thread.sleep(10000);

//            final ProcessBuilder builder = new ProcessBuilder("kill -SIGTERM -"+a);
//            builder.start();
            System.exit(0);
        }
    }
}
