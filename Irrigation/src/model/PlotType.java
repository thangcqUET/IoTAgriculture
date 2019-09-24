package model;

public class PlotType {
    private Integer plotTypeId;
    private String plotType;

    public Integer getPlotTypeId() {
        return plotTypeId;
    }

    public void setPlotTypeId(Integer plotTypeId) {
        this.plotTypeId = plotTypeId;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public PlotType(String plotType) {
        this.plotType = plotType;
    }

    public PlotType(Integer plotTypeId, String plotType) {
        this.plotTypeId = plotTypeId;
        this.plotType = plotType;
    }
}
