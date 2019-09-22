package DAO;

import Connector.DBConnector;

import java.util.List;

public interface Dao<T> {
    DBConnector dbConnector = DBConnector.getInstance();
    public List<T> getAll();
    public T getById(int id);
    public int save(T t);
    public void update(T t_old, T t_new);
    public void delete(long id);
}
