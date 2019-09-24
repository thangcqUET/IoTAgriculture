package model;

public class Locate {
    private Integer locateId;
    private String locate;

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

    public Locate(Integer locateId, String locate) {
        this.locateId = locateId;
        this.locate = locate;
    }
}
