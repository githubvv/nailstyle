package controllers.app;

import db.dao.*;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ManagedBean(name = "dsController", eager = true)
@ApplicationScoped
public class DataSourceController {
    private DataSource ds;
    private ArticleDao articleDao;
    private GalleryDao galleryDao;
    private PriceListDao priceListDao;
    public DataSourceController() {

        try {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/nailstyle");
            createDao(ds);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void createDao(DataSource ds) {
        this.articleDao = new ArticleDaoImpl(ds);
        this.galleryDao = new GalleryDaoImpl(ds);
        this.priceListDao = new PriceListDaoImpl(ds);
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public GalleryDao getGalleryDao() {
        return galleryDao;
    }

    public PriceListDao getPriceListDao() {
        return priceListDao;
    }

}
