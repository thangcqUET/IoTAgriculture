package model;

public class Plot {
    private int plotID;
    private float area;
    private int plotTypeID;
    private int farmID;

    public Plot(int plotID, float area, int plotTypeID, int farmID) {
        this.plotID = plotID;
        this.area = area;
        this.plotTypeID = plotTypeID;
        this.farmID = farmID;
    }

    @Override
    public String toString() {
        return "plotID: "+plotID+", area: "+area+", plotTypeID: "+plotTypeID+", farmID: "+farmID+'\n';
    }

    public int getPlotID() {
        return plotID;
    }

    public void setPlotID(int plotID) {
        this.plotID = plotID;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public int getPlotTypeID() {
        return plotTypeID;
    }

    public void setPlotTypeID(int plotTypeID) {
        this.plotTypeID = plotTypeID;
    }

    public int getFarmID() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }
}
