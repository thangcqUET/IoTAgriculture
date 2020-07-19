package components.autoController;

import model.WeatherForecast;

public class Optimizer {
    int[] irrigationPeriodTimeLevel={0,1,2,3};
    float timeLevelSelected=0;
    HumidityModel humidityModel=new HumidityModel();
    Integer timeSample;
    Integer horizontal=12;
    int[] solution = new int[horizontal];
    float[] bestSolution = new float[horizontal];
    DeviceControlUnit curDCU;
    Result resultOptimize;
    public void resetResult(){
        resultOptimize=null;
    }
    public void process(DeviceControlUnit dcu) {
        curDCU=dcu;
        curDCU.updateCurrentSoilMoisture();
        //trường hợp chuẩn bị có mưa sẽ không tưới cho cây
        if(curDCU.getWeatherForecast().getWeatherForecastAtATimes().get(1).getRainValue()>80){
            return;
        }
        //trong trường hợp độ ẩm đất lớn hơn ngưỡng trên
        if(curDCU.getCurrentSoilMoisture()>curDCU.getUpperThreshold()){
            return;
        }

        // trong trường hợp độ ẩm đất nhỏ hơn ngưỡng dưới
        if(curDCU.getCurrentSoilMoisture()<curDCU.getLowerThreshold()){
            while(curDCU.getCurrentSoilMoisture()<curDCU.getLowerThreshold()){
                Float irrigationPeriodTime=0F;
                System.out.println("Optimizer line 30");
                System.out.println(curDCU.getCurrentSoilMoisture().floatValue()+" "+
                        (curDCU.getLowerThreshold()+curDCU.getCurrentSoilMoisture())/2);
                irrigationPeriodTime = humidityModel.getIrrigationPeriodTime(
                        curDCU.getCurrentSoilMoisture().floatValue(),
                        (curDCU.getLowerThreshold()+curDCU.getCurrentSoilMoisture())/2
                );
//                System.out.println("Optimizer irrigationPeriodTime:" + irrigationPeriodTime);
                curDCU.setIrrigationPeriodTime(irrigationPeriodTime);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                curDCU.updateCurrentSoilMoisture();
            }
        }
        // Trong trường hợp Độ ẩm đất đang nằm ở trong ngưỡng
        resetResult();
        Chart chart = new Chart(curDCU.getUpperThreshold(),curDCU.getLowerThreshold());
        Result result = new Result();
        result.setChart(chart);
        Chart.Point firstPoint = new Chart.Point(curDCU.getCurrentSoilMoisture().floatValue(),curDCU.getLatestIrrigationTime());
        chart.add(firstPoint);

        generateSolution(0, result, curDCU);

        System.out.println("SOLUTION:");
        for(float s : bestSolution){
            System.out.print(s+" ");
        }
        System.out.println();
        curDCU.setIrrigationPeriodTime(timeLevelSelected);

    }

    public void generateSolution(int index, Result result, DeviceControlUnit dcu){
        if(index == horizontal){
            System.out.print("\nSolution");
            System.out.println("\n----------");
            for(int i=0;i<12;i++){
                System.out.print(solution[i]+" ");
            }
            System.out.println("\n----------");
            calResult(result);
            if(resultOptimize==null){
                resultOptimize = new Result(result);
                for(int i=0;i<horizontal;i++){
                    bestSolution[i]=solution[i];
                }
                timeLevelSelected=solution[0];
            }else
                if(resultOptimize.compareTo(result)==-1){
                    System.out.println("\nTemp best solution");
                    System.out.println("-----------");
                    for(int i=0;i<12;i++){
                        System.out.println(solution[i]+" ");
                    }
                    System.out.println(result.getAmountOfIrrigationWater());
                    System.out.println("----------");
                    resultOptimize=new Result(result);
                    for(int i=0;i<horizontal;i++){
                        bestSolution[i]=solution[i];
                    }
                    timeLevelSelected=solution[0];
                }
                return;
            }
            for(int i=0;i<irrigationPeriodTimeLevel.length;i++){
                solution[index]=irrigationPeriodTimeLevel[i];
                if(humidityModel.calNextSoilMoisture(curDCU,result.getChart(),solution[index],dcu.getWeatherForecast().getWeatherForecastAtATimes().get(index))) {
                    calResult(result);
                    if(resultOptimize !=null && resultOptimize.compareTo(result)==1) continue;
                    generateSolution(index + 1, result, dcu);
                    if(solution[index]==0) result.getChart().remove(result.getChart().getPoints().size()-1);
                    else {
                        result.getChart().remove(result.getChart().getPoints().size()-1);
                        result.getChart().remove(result.getChart().getPoints().size()-1);
                    }
                }
            }
    }

    /**
     * From chart to calculate amountOfIrrigationWater and amountOfWaterLost
     * @param result
     */
    public void calResult(Result result){
        Float amountOfIrrigationWater=0F;
        Float amountOfWaterLost = 0F;
        Float curValue = result.getChart().getPoints().get(0).getValue();
        for(int i=1;i<result.getChart().getPoints().size();i++){
            if(result.getChart().getPoints().get(i).getValue()>curValue){
                amountOfIrrigationWater+=(result.getChart().getPoints().get(i).getValue()-curValue);
            }else{
                amountOfWaterLost+=(curValue-result.getChart().getPoints().get(i).getValue());
            }
            curValue=result.getChart().getPoints().get(i).getValue();
        }
        result.setAmountOfIrrigationWater(amountOfIrrigationWater);
        result.setAmountOfWaterLost(amountOfWaterLost);
    }
}
