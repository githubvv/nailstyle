package db.dao;

import beans.PageNavigator;
import db.exceptions.PersistException;
import db.entity.Gallery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GalleryDaoImpl extends AbstractJdbcDao<Gallery, PageNavigator> implements GalleryDao {

    public GalleryDaoImpl(DataSource ds){
        super(ds);
    }

    @Override
    protected String getCreateQuery(Gallery gallery) {
        StringBuilder sb = new StringBuilder("INSERT INTO GALLERY(name, path, date, description) VALUES('").
                append(gallery.getName()).append("','").
                append(gallery.getPath()).append("','").
                append(gallery.getDate()).append("','").
                append(gallery.getDescription()).append("');");
        return sb.toString();
    }

    @Override
    protected String getReadQuery(Integer id) {
        StringBuilder sb = new StringBuilder("SELECT * FROM GALLERY WHERE id =").append(id).append(";");
        return sb.toString();
    }

    @Override
    protected String getTotalCountQuery() {
        return "SELECT COUNT(*) FROM GALLERY";
    }

    @Override
    protected String getListQuery(PageNavigator pageNav) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM GALLERY ORDER BY DATE DESC");
        if (pageNav != null) {
            sqlBuilder
                    .append(" limit ")
                    .append(pageNav.getSelectedPageNumber()
                            * pageNav.getObjOnPage()
                            - pageNav.getObjOnPage()).append(",")
                    .append(pageNav.getObjOnPage());
        }
        return sqlBuilder.toString();
    }

    @Override
    protected String getUpdateQuery(Gallery gallery) {
        StringBuilder sb = new StringBuilder("UPDATE GALLERY SET name='").append(gallery.getName()).append("',").
                append("path='").append(gallery.getPath()).append("',").
                append("date='").append(gallery.getDate()).append("',").
                append("description='").append(gallery.getDescription()).
                append("' WHERE id=").append(gallery.getId());
        return sb.toString();
    }

    @Override
    protected String getDeleteQuery(Integer id) {
        StringBuilder sb = new StringBuilder("DELETE GALLERY WHERE id=").append(id).append(";");
        return sb.toString();
    }

    @Override
    public List<Gallery> parseResultSet(ResultSet rs) throws PersistException {
        List<Gallery> galleryList = new ArrayList<Gallery>();
        try {
            while (rs.next()) {
                Gallery gallery = new Gallery();
                gallery.setId(rs.getInt("id"));
                gallery.setName(rs.getString("name"));
                gallery.setPath(rs.getString("path"));
                gallery.setDate(rs.getDate("date"));
                gallery.setDescription(rs.getString("description"));
                galleryList.add(gallery);
            }
        } catch (SQLException e) {
            throw new PersistException();
        }
        return galleryList;
    }
}