package db.dao;
import beans.PageNavigator;
import db.exceptions.PersistException;
import java.sql.SQLException;
import java.util.List;

public interface Crud<T, PN extends PageNavigator>{

    Integer create(T t) throws PersistException, SQLException;

    T read(Integer id) throws PersistException;

	List<T> getList(PN pageNavigator) throws PersistException;

	void update(T t) throws PersistException;

	void delete(Integer id) throws PersistException;

    int getTotalCount() throws PersistException;

}
