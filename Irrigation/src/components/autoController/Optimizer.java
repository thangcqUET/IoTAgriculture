package components.autoController;

import model.WeatherForecast;

public class Optimizer {
    float[] waterLevel={0,1,2,3};
    float waterLevelSelected=0;
    HumidityModel humidityModel=new HumidityModel();
    Integer timeSample;
    Integer horizontal=12;
    float[] solution = new float[horizontal];
    float[] bestSolution = new float[horizontal];
    DeviceControlUnit curDCU;
    Result resultOptimize;
    public void reset(){
        resultOptimize=null;
    }
    public void process(DeviceControlUnit dcu) {
//        int upperThreshold = deviceControlUnit.upperThreshold;
//        int lowerThreshold = deviceControlUnit.lowerThreshold;
//        int currentHumidity = deviceControlUnit.currentHumidity;
//        WeatherForecast weatherForecast = weatherForecasts.getForecastByLocateID(deviceControlUnit.locateID);
//
//        humidityModel.process(deviceControlUnit);
        reset();
        curDCU=dcu;
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
        curDCU.setAmountOfWater(waterLevelSelected);

    }

    public void generateSolution(int index, Result result, DeviceControlUnit dcu){
        if(index == horizontal){
            //System.out.print(".");
//            for(int i=0;i<12;i++){
//                System.out.print(solution[i]+" ");
//            }
//            System.out.println("\n----------");
            calResult(result);
            if(resultOptimize==null){
                resultOptimize = new Result(result);
            }else
                if(resultOptimize.compareTo(result)==-1){
//                    System.out.println();
//                    for(int i=0;i<12;i++){
//                        System.out.println(solution[i]+" ");
//                    }
//                    System.out.println(result.getAmountOfIrrigationWater());
//                    System.out.println("----------");
                    resultOptimize=new Result(result);
                    for(int i=0;i<horizontal;i++){
                        bestSolution[i]=solution[i];
                    }
                    waterLevelSelected=solution[0];
                }
                return;
            }
            for(int i=0;i<waterLevel.length;i++){
                solution[index]=waterLevel[i];
                if(humidityModel.calNextSoilMoisture(curDCU,result.getChart(),solution[index],dcu.getWeatherForecast().getWeatherForecastAtATimes().get(index))) {
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
