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

    @Override
    public void save(Plot plot) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into Plots(PlotID,Area,PlotTypeID,FarmID) values " +
                    "("+plot.getPlotID()+","+plot.getArea()+","+plot.getPlotTypeID()+","+plot.getFarmID()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Plot t_old, Plot t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
