package model;

public class Farm {
    private Integer farmID;
    private Integer locateId;
    private Double area;
    private Integer farmTypeID;
    private String farmType;
    private Boolean status;
    private Integer userID;

    public Farm(Integer locateId, Double area, Integer farmTypeID,Boolean status, Integer userID) {
        this.locateId = locateId;
        this.area = area;
        this.farmTypeID = farmTypeID;
        this.status = status;
        this.userID = userID;
    }

    public Farm(Integer farmID, Integer locateId, Double area, Integer farmTypeID,Boolean status, Integer userID) {
        this.farmID = farmID;
        this.locateId = locateId;
        this.area = area;
        this.farmTypeID = farmTypeID;
        this.status = status;
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

    public Integer getLocateId() {
        return locateId;
    }

    public void setLocateId(Integer locateId) {
        this.locateId = locateId;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "farmID: "+farmID+", farmType: "+farmType+", locateId: "+locateId+", area: "+area+", userId: "+userID+"\n";
    }
}
