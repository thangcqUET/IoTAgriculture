package components.autoController;

import components.Converter;
import model.WeatherForecast;

import java.time.LocalTime;
import java.util.ArrayList;

public class HumidityModel {
    /**
     * Tính độ ẩm đất sau khi nước bay hơi
     * @param period
     * @param curSoilMoisture
     * @param curWat
     * @return
     */
    Float getNextSoilMoisture(Float period /* s */, Float curSoilMoisture, WeatherForecast.WeatherForecastAtATime curWat){
        Float nextSoilMoisture=curSoilMoisture;
        Float amountOfWaterLost=(4*period)/3600F;//cong thuc
        nextSoilMoisture-=amountOfWaterLost;
        return nextSoilMoisture;
    }
    Float getNextSoilMoisture(Float curSoilMoisture, Float pumpSpeed, Float period){
        Float ret=getNextSoilMoisture(curSoilMoisture,pumpSpeed*period);
        return ret;
    }

    /**
     * Tính độ ẩm đất sau khi tưới nước.
     * @param curSoilMoisture
     * @param IrrigationVolume
     * @return
     */
    Float getNextSoilMoisture(Float curSoilMoisture, Float IrrigationVolume){
        Float ret=curSoilMoisture;
        //tim phu thuoc
        ret+=IrrigationVolume;
        return ret;
    }
    Long getIrrigationPeriodTime(Float irrigationVolume, Float pumpSpeed){
        Float period =(irrigationVolume/pumpSpeed);//(pumpSpeed: ml/s)
        return period.longValue();
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
    public boolean calNextSoilMoisture(DeviceControlUnit dcu, Chart chart, Float water, WeatherForecast.WeatherForecastAtATime wat) {
        if(water==0){
            //Tính lượng giảm
            Float curSoilMoisture =  chart.getPoints().get(chart.getPoints().size()-1).getValue();
            LocalTime curTime = chart.getPoints().get(chart.getPoints().size()-1).getTime();

            Float nextSoilMoisture = getNextSoilMoisture(1F*60*60, curSoilMoisture, wat);
            LocalTime nextTime = curTime.plusHours(1);

            Chart.Point nextPoint = new Chart.Point(nextSoilMoisture,nextTime);
            chart.add(nextPoint);
            if(chart.getOutOfRange()){
                chart.remove(chart.getPoints().size()-1);
                return false;
            }
        }else{
            //Tính lượng tăng
            Float curSoilMoisture =  chart.getPoints().get(chart.getPoints().size()-1).getValue();
            LocalTime curTime = chart.getPoints().get(chart.getPoints().size()-1).getTime();

            Long periodIrrigation = getIrrigationPeriodTime(water,dcu.getPumpSpeed()).longValue();
            LocalTime nextTime1 = curTime.plusSeconds(periodIrrigation);
            Float nextSoilMoisture1 = getNextSoilMoisture(curSoilMoisture, water);
            Chart.Point nextPoint1 = new Chart.Point(nextSoilMoisture1,nextTime1);
            chart.add(nextPoint1);
            if(chart.getOutOfRange()){
                chart.remove(chart.getPoints().size()-1);
                return false;
            }
            //Tính lượng giảm
            Float nextSoilMoisture2 = getNextSoilMoisture(1F*60*60-periodIrrigation,nextSoilMoisture1,wat);
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
