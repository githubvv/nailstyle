package db.dao;
import beans.PageNavigator;
import db.entity.Article;
import db.entity.ArticleImages;
import db.exceptions.PersistException;

import java.util.List;

public interface ArticleDao extends Crud<Article, PageNavigator> {

    List<ArticleImages> getImagesByArticle(Integer articleId) throws PersistException;

    void removeImgFromArticle(ArticleImages articleImg) throws PersistException;

    void addImgToArticle(ArticleImages articleImg, Integer articleId)  throws PersistException;

}
