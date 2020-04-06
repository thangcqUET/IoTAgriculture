package components.autoController;

public class Result implements Comparable{
    private Float amountOfIrrigationWater;
    private Float amountOfWaterLost;
    Chart chart;
    public Result(Float amountOfIrrigationWater, Float amountOfWaterLost) {
        this.amountOfIrrigationWater = amountOfIrrigationWater;
        this.amountOfWaterLost = amountOfWaterLost;
    }

    public Result() {
    }

    public Result(Result result){
        this.amountOfIrrigationWater = result.getAmountOfIrrigationWater().floatValue();
        this.amountOfWaterLost = result.getAmountOfWaterLost().floatValue();
        chart = new Chart(result.getChart());
    }
    public Float getAmountOfIrrigationWater() {
        return amountOfIrrigationWater;
    }

    public void setAmountOfIrrigationWater(Float amountOfIrrigationWater) {
        this.amountOfIrrigationWater = amountOfIrrigationWater;
    }

    public Float getAmountOfWaterLost() {
        return amountOfWaterLost;
    }

    public void setAmountOfWaterLost(Float amountOfWaterLost) {
        this.amountOfWaterLost = amountOfWaterLost;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof Result){
            if(((Result) o).getAmountOfIrrigationWater()<this.amountOfIrrigationWater){
                return -1;
            }else if(((Result) o).getAmountOfIrrigationWater()>this.amountOfIrrigationWater){
                return 1;
            }else return 0;
        }
        else
        return Integer.MIN_VALUE;
    }
}
