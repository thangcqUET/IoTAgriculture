package components.autoController;

import model.WeatherForecast;

import java.io.IOException;
import java.time.LocalTime;

import model.WeatherForecastAtATime;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

public class HumidityModel {
    private MultiLayerNetwork evaModel;
    private final float maxSoilMoisture = 100;
    private final float minSoilMoisture = 0;
    private final float maxHumidity = 100;
    private final float minHumidity = 0;
    private final float maxTemperature = 50;
    private final float minTemperature = 10;

    private final float increase_dSoilMoisture = 1.2F; //độ ẩm tăng sau khi tưới 1s nước: 0.4653F đo được
    public HumidityModel(){
        // load the model
        try {
            evaModel = KerasModelImport.importKerasSequentialModelAndWeights("eva_hourly.h5");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKerasConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
    }

    public float scaleMinMax(float minValue, float maxValue, float minRange, float maxRange, float value){
        return minRange+((value-minValue)*(maxRange-minRange))/(maxValue-minValue);
    }

    /**
     * Tính độ ẩm đất sau khi nước bay hơi sau 1 gio
     * @param curSoilMoisture
     * @param curWat
     * @return
     */
    public Float getNextHourSoilMoisture(Float curSoilMoisture, WeatherForecastAtATime curWat){
        float humidity = curWat.getRelativeHumidity();
        float temperature = curWat.getTemperature();
        float soilmoisture = curSoilMoisture;
        soilmoisture = scaleMinMax(minSoilMoisture,maxSoilMoisture,0,1,soilmoisture);
        humidity = scaleMinMax(minHumidity,maxHumidity,0,1,humidity);
        temperature = scaleMinMax(minTemperature,maxTemperature,0,1,temperature);
        float[] input = {soilmoisture,humidity,temperature};
        INDArray features = Nd4j.create(input);
        Float nextSoilMoisture = evaModel.output(features).getFloat(0);
        nextSoilMoisture = scaleMinMax(0,1,minSoilMoisture,maxSoilMoisture,nextSoilMoisture);
        return nextSoilMoisture;
    }

//    public Float getNextHourSoilMoisture(Float curSoilMoisture, WeatherForecastAtATime curWat){
//        float nextSM = curSoilMoisture;
//        nextSM = getNextSoilMoisture(nextSM,curWat);
//        return nextSM;
//    }

//    public Float getNextSoilMoisture(Float curSoilMoisture, Float pumpSpeed, Float period){
//        Float ret=getNextSoilMoisture(curSoilMoisture,pumpSpeed*period);
//        return ret;
//    }

    /**
     * Tính độ ẩm đất sau khi tưới nước.
     * @param curSoilMoisture %/s
     * @param irrigationPeriodTime s
     * @return
     */
    public Float getNextSoilMoisture(Float curSoilMoisture, Integer irrigationPeriodTime){
        Float ret=curSoilMoisture;
        //tim phu thuoc
        ret+=(increase_dSoilMoisture*irrigationPeriodTime);
        return ret;
    }
//    public Long getIrrigationPeriodTime(Float irrigationVolume, Float pumpSpeed){
//        Float period =(irrigationVolume/pumpSpeed);//(pumpSpeed: ml/s)
//        return period.longValue();
//    }

    /**
     * Tính thời gian tưới để đạt được độ ẩm đích
     * @param currentSoilMoisture
     * @param destSoilMoisture
     * @return
     */
    public Float getIrrigationPeriodTime(Float currentSoilMoisture, Float destSoilMoisture){
        if(currentSoilMoisture>=destSoilMoisture){
            return 0F;
        }
        return (destSoilMoisture-currentSoilMoisture)/increase_dSoilMoisture;
    }


    /**
     * Tính các trạng thái độ ẩm của đất sau khi tưới một lượng nước là water trong thời gian 1 giờ. water là 0 có nghĩa là không tưới.
     * Nếu độ ẩm nằm trong ngưỡng thì trả về true và lưu độ ẩm vào chart, nếu không thì trả về false và không lưu vào chart
     * @param dcu
     * @param chart: danh sách biến thiên độ ẩm
     * @param water
     * @param wat
     * @return true: nếu nằm trong ngưỡng; false: nằm ngoài ngưỡng
     */
//    public boolean calNextSoilMoisture(DeviceControlUnit dcu, Chart chart, Float water, WeatherForecastAtATime wat) {
//        if(water==0){
//            //Tính lượng giảm
//            Float curSoilMoisture =  chart.getPoints().get(chart.getPoints().size()-1).getValue();
//            LocalTime curTime = chart.getPoints().get(chart.getPoints().size()-1).getTime();
//
//            Float nextSoilMoisture = getNextHourSoilMoisture(curSoilMoisture, wat);
//            LocalTime nextTime = curTime.plusHours(1);
//
//            Chart.Point nextPoint = new Chart.Point(nextSoilMoisture,nextTime);
//            chart.add(nextPoint);
//            if(chart.getOutOfRange()){
//                chart.remove(chart.getPoints().size()-1);
//                return false;
//            }
//        }else{
//            //Tính lượng tăng
//            Float curSoilMoisture =  chart.getPoints().get(chart.getPoints().size()-1).getValue();
//            LocalTime curTime = chart.getPoints().get(chart.getPoints().size()-1).getTime();
//
//            Long periodIrrigation = getIrrigationPeriodTime(water,dcu.getPumpSpeed()).longValue();
//            LocalTime nextTime1 = curTime.plusSeconds(periodIrrigation);
//            Float nextSoilMoisture1 = getNextSoilMoisture(curSoilMoisture, water);
//            Chart.Point nextPoint1 = new Chart.Point(nextSoilMoisture1,nextTime1);
//            chart.add(nextPoint1);
//            if(chart.getOutOfRange()){
//                chart.remove(chart.getPoints().size()-1);
//                return false;
//            }
//            //Tính lượng giảm
//            Float nextSoilMoisture2 = getNextHourSoilMoisture(nextSoilMoisture1,wat);
//            LocalTime nextTime2 = curTime.plusHours(1);
//            Chart.Point nextPoint2 = new Chart.Point(nextSoilMoisture2,nextTime2);
//            chart.add(nextPoint2);
//            if(chart.getOutOfRange()){
//                chart.remove(chart.getPoints().size()-1);
//                chart.remove(chart.getPoints().size()-1);
//                return false;
//            }
//            return true;
//        }
//        return true;
//    }

    /**
     * Tính các trạng thái độ ẩm của đất sau khi tưới một lượng nước là water trong thời gian 1 giờ. water là 0 có nghĩa là không tưới.
     * Nếu độ ẩm nằm trong ngưỡng thì trả về true và lưu độ ẩm vào chart, nếu không thì trả về false và không lưu vào chart
     * @param dcu
     * @param chart: danh sách biến thiên độ ẩm
     * @param irrigationPeriodTime
     * @param wat
     * @return true: nếu nằm trong ngưỡng; false: nằm ngoài ngưỡng
     */
    public boolean calNextSoilMoisture(DeviceControlUnit dcu, Chart chart, Integer irrigationPeriodTime, WeatherForecastAtATime wat) {
        if(irrigationPeriodTime==0){
            //Tính lượng giảm
            Float curSoilMoisture =  chart.getPoints().get(chart.getPoints().size()-1).getValue();
            LocalTime curTime = chart.getPoints().get(chart.getPoints().size()-1).getTime();

            Float nextSoilMoisture = getNextHourSoilMoisture(curSoilMoisture, wat);
            LocalTime nextTime = curTime.plusHours(1);

            Chart.Point nextPoint = new Chart.Point(nextSoilMoisture,nextTime);
            chart.add(nextPoint);
            if(chart.getOutOfRange()){
                chart.remove(chart.getPoints().size()-1);
                return false;
            }
        }else{
            //Tính lượng tăng !!!
            Float curSoilMoisture =  chart.getPoints().get(chart.getPoints().size()-1).getValue();
            LocalTime curTime = chart.getPoints().get(chart.getPoints().size()-1).getTime();
            LocalTime nextTime1 = curTime.plusSeconds(irrigationPeriodTime);
            Float nextSoilMoisture1 = getNextSoilMoisture(curSoilMoisture, irrigationPeriodTime);
            Chart.Point nextPoint1 = new Chart.Point(nextSoilMoisture1,nextTime1);
            chart.add(nextPoint1);
            if(chart.getOutOfRange()){
                chart.remove(chart.getPoints().size()-1);
                return false;
            }
            //Tính lượng giảm
            Float nextSoilMoisture2 = getNextHourSoilMoisture(nextSoilMoisture1,wat);
            LocalTime nextTime2 = curTime.plusHours(1);
            Chart.Point nextPoint2 = new Chart.Point(nextSoilMoisture2,nextTime2);
            chart.add(nextPoint2);
            if(chart.getOutOfRange()){
                chart.remove(chart.getPoints().size()-1);
                chart.remove(chart.getPoints().size()-1);
                return false;
            }
            return true;
        }
        return true;
    }
}
