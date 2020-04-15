import Utilities.Helper;
import components.WeatherForecastCollector;
import components.autoController.DeviceControlUnit;
import model.Forecast;
import model.WeatherForecast;
import model.WeatherForecastAtATime;
import org.apache.log4j.BasicConfigurator;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    public static void main(String[] args)  {
        testForecastCollector();
    }

    public static void testKeras() throws ParseException, InterruptedException, IOException {
        BasicConfigurator.configure();
        WeatherForecast wf = new WeatherForecast();
        wf.update();
        WeatherForecastAtATime wfaat=wf.getWeatherForecastAtATimes().get(0);
        float humidity = wfaat.getRelativeHumidity();
        float temperature = wfaat.getTemperature();
        float soilmoisture = 80;
        float light = 255;
        System.out.println(wfaat);
        System.out.println(humidity+"\n"+temperature+"\n"+soilmoisture+"\n"+light);
        try {
            MultiLayerNetwork model = null;
            model = KerasModelImport.importKerasSequentialModelAndWeights("eva.h5");
            float[] input = {soilmoisture,humidity,temperature,light};
//            float[] input = {0.11755237334235569F, 0.62227602905569F, 0.11445783132530107F, 0.0F};
            INDArray features = Nd4j.create(input);
            Float result = model.output(features).getFloat(0);
            System.out.println(result);
        } catch (InvalidKerasConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void testForecastCollector(){
        WeatherForecastCollector weatherForecastCollector = new WeatherForecastCollector();
        Thread weatherForecastCollectorThread = new Thread(weatherForecastCollector);
        weatherForecastCollectorThread.start();
    }
}
