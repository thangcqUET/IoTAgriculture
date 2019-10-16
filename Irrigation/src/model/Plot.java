package model;

public class Plot {
    private Integer plotID;
    private Float area;
    private Integer plotTypeID;
    private Integer farmID;

    public Plot(Integer plotID, Float area, Integer plotTypeID, Integer farmID) {
        this.plotID = plotID;
        this.area = area;
        this.plotTypeID = plotTypeID;
        this.farmID = farmID;
    }

    public Plot(Float area, Integer plotTypeID, Integer farmID) {
        this.area = area;
        this.plotTypeID = plotTypeID;
        this.farmID = farmID;
    }

    @Override
    public String toString() {
        return "plotID: "+plotID+", area: "+area+", plotTypeID: "+plotTypeID+", farmID: "+farmID;
    }

    public Integer getPlotID() {
        return plotID;
    }

    public void setPlotID(Integer plotID) {
        this.plotID = plotID;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public Integer getPlotTypeID() {
        return plotTypeID;
    }

    public void setPlotTypeID(Integer plotTypeID) {
        this.plotTypeID = plotTypeID;
    }

    public Integer getFarmID() {
        return farmID;
    }

    public void setFarmID(Integer farmID) {
        this.farmID = farmID;
    }
}
