package model;

public class FarmType {
    private Integer farmTypeId;
    private String farmType;

    public Integer getFarmTypeId() {
        return farmTypeId;
    }

    public void setFarmTypeId(Integer farmTypeId) {
        this.farmTypeId = farmTypeId;
    }

    public String getFarmType() {
        return farmType;
    }

    public void setFarmType(String farmType) {
        this.farmType = farmType;
    }

    public FarmType(String farmType) {
        this.farmType = farmType;
    }

    public FarmType(Integer farmTypeId, String farmType) {
        this.farmTypeId = farmTypeId;
        this.farmType = farmType;
    }
}
