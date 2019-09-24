package model;

public class User {
    private Integer userID;
    private String userName;
    private String passwordMD5;
    public User(String userName, String passwordMD5){
        this.userName=userName;
        this.passwordMD5= passwordMD5;
    }

    public User(Integer userID, String userName, String passwordMD5) {
        this.userID = userID;
        this.userName = userName;
        this.passwordMD5 = passwordMD5;
    }

    @Override
    public String toString() {
        return "userID: "+userID+", userName: "+userName+", passwordMD5: "+passwordMD5+"\n";
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return passwordMD5;
    }

    public void setPassword(String passwordMD5) {
        this.passwordMD5 = passwordMD5;
    }
}
