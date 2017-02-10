package db.dao;

import beans.PageNavigator;
import db.entity.Article;
import db.entity.ArticleImages;
import db.exceptions.PersistException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArticleDaoImpl extends AbstractJdbcDao<Article, PageNavigator> implements ArticleDao {
    private DataSource ds;

    public ArticleDaoImpl(DataSource ds) {
        super(ds);
        this.ds = ds;
    }

    @Override
    public Integer create(Article article) throws PersistException, SQLException {
        Connection connection = null;
        try {
            connection = getDs().getConnection();
            Integer articleId = super.create(article);
            String artImgSql = getCreateArtImages(article, articleId);
            try (PreparedStatement pstmt = connection.prepareStatement(artImgSql, Statement.RETURN_GENERATED_KEYS);) {
                pstmt.executeUpdate();
                connection.commit();
                return articleId;
            } catch (SQLException e) {
                connection.rollback();
                throw new PersistException(e);
            }
        } catch (PersistException e) {
            throw new PersistException(e);
        }
        finally {
            if(connection!=null)
                connection.close();
        }
   }

    @Override
    protected String getReadQuery(Integer id) {
        return "SELECT * FROM ARTICLE WHERE id = ?;" + id;
    }

    @Override
    public String getCreateQuery(Article article) {
        StringBuilder sb = new StringBuilder("INSERT INTO ARTICLE (name, text, date, description) VALUES ('").
                append(article.getName()).append("','").
                append(article.getText()).append("','").
                append(article.getDate()).append("','").
                append(article.getDescription()).append("');");
        return sb.toString();
    }

    public String getCreateArtImages(Article article, Integer articleId) {
        StringBuilder sb = new StringBuilder("");
        for (ArticleImages artImg : article.getArticleImgList()) {
            sb.append("INSERT INTO ARTICLEIMAGES (articleId, name) VALUES ('").
                    append(articleId).append("','").
                    append(artImg.getName()).append("');");
        }
        return sb.toString();
    }

    @Override
    public String getUpdateQuery(Article article) {
        StringBuilder sb = new StringBuilder("UPDATE ARTICLE SET name ='").append(article.getName())
                .append("',text='").append(article.getText())
                .append("',date='").append(article.getDate())
                .append("',description='").append(article.getDescription())
                .append("' WHERE id=").append(article.getId()).append(";");
        sb.append("DELETE ARTICLEIMAGES WHERE articleID =").append(article.getId()).append(";");
        /*for(ArticleImages artImg:article.getArticleImgList()){
            sb.append("UPDATE ARTICLEIMAGES SET articleId ='").append(artImg.getArticleId())
                    .append("',name='").append(artImg.getName())
                    .append("' WHERE id=").append(artImg.getId()).append(";");}*/
        for (ArticleImages artImg : article.getArticleImgList()) {
            sb.append("INSERT INTO ARTICLEIMAGES (articleId, name) VALUES ('").
                    append(artImg.getArticleId()).append("','").
                    append(artImg.getName()).append("');");
        }
        return sb.toString();
    }

    @Override
    public String getDeleteQuery(Integer id) {
        //DELETE ARTICLE IMAGES
        StringBuilder sb = new StringBuilder("DELETE ARTICLEIMAGES WHERE articleID =").append(id).append(";");
        //DELETE ARTICLE
        sb.append("DELETE ARTICLE WHERE ID=").append(id).append(";");
        return sb.toString();
    }

    @Override
    protected String getTotalCountQuery() {
        return "SELECT COUNT(*) FROM ARTICLE";
    }

    @Override
    public String getListQuery(PageNavigator pageNav) {

        StringBuilder sqlBuilder = new StringBuilder("SELECT ART.ID AS ART_ID, ART.NAME AS ART_NAME,ART.TEXT AS ART_TEXT,ART.DATE AS ART_DATE,ART.DESCRIPTION AS ART_DESCR ,IMG.ID AS IMG_ID,IMG.ARTICLEID AS IMG_ARTID,IMG.NAME AS IMG_NAME FROM ARTICLEIMAGES AS IMG RIGHT JOIN (SELECT T.ID,T.NAME,T.TEXT,T.DATE,T.DESCRIPTION FROM ARTICLE AS T ORDER BY T.DATE  DESC, T.ID ");
        if (pageNav != null) {
            sqlBuilder
                    .append(" limit ")
                    .append(pageNav.getSelectedPageNumber() * pageNav.getObjOnPage() - pageNav.getObjOnPage()).
                    append(",").
                    append(pageNav.getObjOnPage()).
                    append(") AS ART ON ART.ID=IMG.ARTICLEID");
        }
        return sqlBuilder.toString();
    }

    @Override
    public List<Article> parseResultSet(ResultSet rs) throws PersistException {
        List<Article> articleList = new ArrayList<>();

        try {
            int prevId = 0;
            Article article = null;
            while (rs.next()) {
                if (prevId != rs.getInt("ART_ID")) {
                    article = new Article();
                    article.setId(rs.getInt("ART_ID"));
                    article.setName(rs.getString("ART_NAME"));
                    article.setText(rs.getString("ART_TEXT"));
                    article.setDate(rs.getDate("ART_DATE"));
                    article.setDescription(rs.getString("ART_DESCR"));
                    articleList.add(article);
                    prevId = rs.getInt("ART_ID");
                }
                if (rs.getInt("IMG_ARTID") != 0)
                    article.addArtImg(new ArticleImages(rs.getInt("IMG_ID"), rs.getInt("IMG_ARTID"), rs.getString("IMG_NAME")));
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return articleList;
    }

    @Override
    public List<ArticleImages> getImagesByArticle(Integer articleId) throws PersistException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeImgFromArticle(ArticleImages articleImg) throws PersistException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addImgToArticle(ArticleImages articleImg, Integer articleId) throws PersistException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
