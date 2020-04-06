package DAO;

import model.Farm;
import model.Plot;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlotDao implements Dao<Plot> {
    @Override
    public List<Plot> getAll() {
        Statement statement= null;
        List<Plot> plots = new ArrayList<Plot>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Plots";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Plot plot = new Plot(resultSet.getInt("PlotID"),
                        resultSet.getFloat("Area"),
                        resultSet.getInt("PlotTypeID"),
                        resultSet.getInt("FarmID")
                );
                plots.add(plot);
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
        return plots;
    }

    public List<Plot> getByFarmId(Integer farmId){
        Statement statement= null;
        List<Plot> plots = new ArrayList<Plot>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Plots where FarmID = "+farmId;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Plot plot = new Plot(resultSet.getInt("PlotID"),
                        resultSet.getFloat("Area"),
                        resultSet.getInt("PlotTypeID"),
                        resultSet.getInt("FarmID")
                );
                plots.add(plot);
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
        return plots;
    }
    @Override
    public Plot getById(int id) {
        Statement statement;
        Plot plot = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from Plots where PlotID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                plot = new Plot(resultSet.getInt("PlotID"),
                        resultSet.getFloat("Area"),
                        resultSet.getInt("PlotTypeID"),
                        resultSet.getInt("FarmID")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plot;
    }
    public Integer getFarmIdById(int id) {
        Statement statement;
        Integer farmId = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select FarmID from Plots where PlotID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                farmId = resultSet.getInt("FarmID");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmId;
    }

    @Override
    public int save(Plot plot) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Plots(Area,PlotTypeID,FarmID) values " +
                    "("+plot.getArea()+","+plot.getPlotTypeID()+","+plot.getFarmID()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Plot t_old, Plot t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
