package model;

public class User {
    private int userID;
    private String userName;
    private String passwordMD5;
    public User(String userName, String passwordMD5){
        this.userName=userName;
        this.passwordMD5= passwordMD5;
    }

    public User(int userID, String userName, String passwordMD5) {
        this.userID = userID;
        this.userName = userName;
        this.passwordMD5 = passwordMD5;
    }

    @Override
    public String toString() {
        return "userID: "+userID+", userName: "+userName+", passwordMD5: "+passwordMD5+"\n";
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
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
