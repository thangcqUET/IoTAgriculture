package model;

public class Locate {
    private String locateId;
    private String locate;


    public String getLocateId() {
        return locateId;
    }

    public void setLocateId(String locateId) {
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

    public Locate(String locateId, String locate) {
        this.locateId = locateId;
        this.locate = locate;

    }
}
