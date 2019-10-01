package model;

public class Locate {
    private Integer locateId;
    private String locate;
    private Float lon;
    private Float lat;

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Integer getLocateId() {
        return locateId;
    }

    public void setLocateId(Integer locateId) {
        this.locateId = locateId;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public Locate(String locate) {
        this.locate = locate;
    }

    public Locate(Integer locateId, String locate, Float lon, Float lat) {
        this.locateId = locateId;
        this.locate = locate;
        this.lon = lon;
        this.lat = lat;
    }
}
