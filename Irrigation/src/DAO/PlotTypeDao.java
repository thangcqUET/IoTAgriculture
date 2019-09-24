package DAO;

import model.FarmType;
import model.PlotType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlotTypeDao implements Dao<PlotType>{
    @Override
    public List<PlotType> getAll() {
        Statement statement= null;
        List<PlotType> plotTypes = new ArrayList<PlotType>();
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from PlotTypes";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                PlotType plotType = new PlotType(resultSet.getInt("PlotTypeID"),
                        resultSet.getString("PlotType")
                );
                plotTypes.add(plotType);
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
        return plotTypes;
    }

    @Override
    public PlotType getById(int id) {
        Statement statement;
        PlotType plotType = null;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "select * from PlotTypes where PlotTypeID = "+id+";";
            ResultSet resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                plotType = new PlotType(resultSet.getInt("PlotTypeID"),
                        resultSet.getString("PlotType")
                );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plotType;
    }

    @Override
    public int save(PlotType plotType) {
        Statement statement;
        try {
            statement = dbConnector.getConnection().createStatement();
            String sql = "insert into PlotTypes(PlotTypeID, PlotType) values " +
                    "("+plotType.getPlotTypeId()+","+plotType.getPlotType()+")";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(PlotType t_old, PlotType t_new) {

    }

    @Override
    public void delete(long id) {

    }
}
