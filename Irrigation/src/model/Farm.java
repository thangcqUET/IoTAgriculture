package model;

public class Farm {
    private Integer farmID;
    private Integer locate;
    private Double area;
    private Integer farmTypeID;
    private String farmType;
    private Integer userID;

    public Farm(Integer locate, Double area, Integer farmTypeID, Integer userID) {
        this.locate = locate;
        this.area = area;
        this.farmTypeID = farmTypeID;
        this.userID = userID;
    }

    public Farm(Integer farmID, Integer locate, Double area, Integer farmTypeID, Integer userID) {
        this.farmID = farmID;
        this.locate = locate;
        this.area = area;
        this.farmTypeID = farmTypeID;
        //create FarmType
        //code here
        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getFarmTypeID() {
        return farmTypeID;
    }

    public String getFarmType() {
        return farmType;
    }

    public Integer getFarmID() {
        return farmID;
    }

    public void setFarmID(Integer farmID) {
        this.farmID = farmID;
    }

    public Integer getLocate() {
        return locate;
    }

    public void setLocate(Integer locate) {
        this.locate = locate;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getfarmTypeID() {
        return farmTypeID;
    }

    public void setfarmTypeID(Integer farmTypeID) {
        this.farmTypeID = farmTypeID;
    }

    public void setFarmType(String farmType) {
        this.farmType = farmType;
    }

    public void setFarmTypeID(Integer farmTypeID) {
        this.farmTypeID = farmTypeID;
    }

    @Override
    public String toString() {
        return "farmID: "+farmID+", farmType: "+farmType+", locate: "+locate+", area: "+area+"\n";
    }
}
