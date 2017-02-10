package db.dao;

import beans.PageNavigator;
import controllers.app.DataSourceController;
import db.exceptions.PersistException;

import javax.faces.bean.ManagedProperty;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJdbcDao<T, PN extends PageNavigator> implements Crud<T, PN> {
    private DataSource ds;

    public AbstractJdbcDao(DataSource ds){
        this.ds = ds;
     }

    public DataSource getDs() {
        return ds;
    }

    @Override
    public int getTotalCount() throws PersistException {
        String sqlQuery = getTotalCountQuery();
        try (Connection connection = ds.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery();) {
             while (rs.next()){
                 return rs.getInt(1);
             }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return 0;
    }

    @Override
    public Integer create(T obj) throws PersistException, SQLException {
        String sql = getCreateQuery(obj);
        Integer id = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return (Integer) id;
    }

    @Override
    public T read(Integer id) throws PersistException {
        String sqlQuery = getReadQuery(id);
        try (Connection connection = ds.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery();) {
             List<T> list = parseResultSet(rs);
             return (list.size()==0)? null :list.get(0);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    @Override
    public List<T> getList(PN pageNav) throws PersistException {
        String sqlQuery = getListQuery(pageNav);
        List<T> list = new ArrayList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery();) {
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return list;
    }

    @Override
    public void update(T obj) throws PersistException {
        String sql = getUpdateQuery(obj);
        try (Connection connection = ds.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    @Override
    public void delete(Integer id) throws PersistException {
        String sql = getDeleteQuery(id);
        try (Connection connection = ds.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistException(e);
        }
    }

    protected abstract String getCreateQuery(T obj);

    protected abstract String getListQuery(PageNavigator pageNav);

    protected abstract String getReadQuery(Integer id);

    protected abstract String getUpdateQuery(T obj);

    protected abstract String getDeleteQuery(Integer id);

    protected abstract String getTotalCountQuery();

    protected abstract List<T> parseResultSet(ResultSet rs)
            throws PersistException;

}
