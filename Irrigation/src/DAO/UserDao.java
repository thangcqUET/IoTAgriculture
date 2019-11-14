package DAO;

import Utilities.Helper;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {
    @Override
    public List<User> getAll() {
        Statement statement= null;
        List<User> users = new ArrayList<User>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Users";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                User user = new User(resultSet.getInt("UserID"),
                        resultSet.getString("UserName"),
                        resultSet.getString("UPassword"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return users;
    }

    public User getByUsernameAndPassword(String username, String password){
        User user = null;
        Statement statement=null;
        String upassword = Helper.md5(password);
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "Select * from Users where UserName = '" + username+"' and UPassword = '"+upassword+"';";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                user = new User(resultSet.getInt("UserID"),
                        resultSet.getString("UserName"),
                        resultSet.getString("UPassword"));
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }
    @Override
    public User getById(int id) {
        Statement statement=null;
        User user = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "Select * from Users where UserID = " + id;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                user = new User(resultSet.getInt("UserID"),
                        resultSet.getString("UserName"),
                        resultSet.getString("UPassword"));
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }


    @Override
    public int save(User user) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Users(UserName,UPassword) values " +
                    "('"+user.getUserName()+"','"+user.getPassword()+"')";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(User t_old, User t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
