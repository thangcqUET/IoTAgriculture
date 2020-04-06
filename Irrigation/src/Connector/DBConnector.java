package Connector;

import model.*;

import java.sql.*;

public class DBConnector{
    private static volatile DBConnector dbConnector = null;
    private String JDBC_DRIVER;
    private String DB_URL;
    private String USER;
    private String PASS;
    private Connection connection;
    private DBConnector() {
        JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
        DB_URL="jdbc:mysql://localhost:3306/irrigation_database";
        USER="root";
        PASS="iotlab2018";
        try {
            Class.forName(JDBC_DRIVER);
            connection =  DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DBConnector getInstance(){
        if(dbConnector == null){
            dbConnector = new DBConnector();
        }
        return dbConnector;
    }

    public Connection getConnection() throws SQLException {
            //May be error, need to recheck
        if(connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return connection;
    }






    public void test(){
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "select * from Devices;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int a = resultSet.getInt("DeviceTypeID");
                String sql1 = "select * from DeviceTypes where DeviceTypeID = "+a+";";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(sql1);
                while (resultSet1.next()){
                    System.out.println(resultSet1.getString("DeviceType"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
