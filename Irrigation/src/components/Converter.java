package components;

public class Converter {
    private static Float pumpSpeed = 10F; //ml/s
    public static Float amountOfWaterToIrrigationTime(Float amountOfWater){//ml
        return amountOfWater/pumpSpeed;//s
    }
    public static Float amountOfWaterToSoilMoisture(Float amountOfWater){//ml
        Float ret=0F;
        return ret;
    }
}
