package model;

public class Farm {
    private int farmID;
    private String locate;
    private double area;
    private int farmTypeID;
    private String farmType;
    private int userID;

    public Farm(String locate, double area, int farmTypeID, int userID) {
        this.locate = locate;
        this.area = area;
        this.farmTypeID = farmTypeID;
        this.userID = userID;
    }

    public Farm(int farmID, String locate, double area, int farmTypeID, int userID) {
        this.farmID = farmID;
        this.locate = locate;
        this.area = area;
        this.farmTypeID = farmTypeID;
        //create FarmType
        //code here
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFarmTypeID() {
        return farmTypeID;
    }

    public String getFarmType() {
        return farmType;
    }

    public int getFarmID() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getfarmTypeID() {
        return farmTypeID;
    }

    public void setfarmTypeID(int farmTypeID) {
        this.farmTypeID = farmTypeID;
    }

    public void setFarmType(String farmType) {
        this.farmType = farmType;
    }

    public void setFarmTypeID(int farmTypeID) {
        this.farmTypeID = farmTypeID;
    }

    @Override
    public String toString() {
        return "farmID: "+farmID+", farmType: "+farmType+", locate: "+locate+", area: "+area+"\n";
    }
}
