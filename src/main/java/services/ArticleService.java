package services;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.dao.ArticleDao;
import db.entity.Article;
import db.entity.ArticleImages;
import db.exceptions.PersistException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ManagedBean(name = "articleService")
@ApplicationScoped
public class ArticleService {

    @ManagedProperty(value = "#{dsController.articleDao}")
    private ArticleDao articleDao;

    @ManagedProperty(value = "#{loadContextHolder}")
    private LoadContextHolder loadContextHolder;

    public ArticleService() {
    }

    public LoadContextHolder getLoadContextHolder() {
        return loadContextHolder;
    }

    public void setLoadContextHolder(LoadContextHolder loadContextHolder) {
        this.loadContextHolder = loadContextHolder;
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

  /*  public ArticleImagesDao getArticleImagesDao() {
        return articleImagesDao;
    }

    public void setArticleImagesDao(ArticleImagesDao articleImagesDao) {
        this.articleImagesDao = articleImagesDao;
    }*/

    public List<Article> getArticles(PageNavigator pn) throws PersistException {
        if (pn.getSelectedPageNumber() == 1)
            return (List<Article>) loadContextHolder.getCacheEntityList("db.entity.Article");
        else return articleDao.getList(pn);
    }

   /* public List<ArticleImages> getArticleImages(Integer articleId) throws PersistException {
        return articleImagesDao.getImagesByArticle(articleId);
    }*/

    public String getArticleImgPath() {
        return loadContextHolder.getPropValues("art_img_path");
    }

    public Article getArticleDemo() {
        Article article = new Article();
        if (loadContextHolder.getCacheEntityList("db.entity.Article").size() > 0)
            article = (Article) loadContextHolder.getCacheEntityList("db.entity.Article").get(0);
        return article;
    }

    public void createArtInDB(Article article) throws PersistException {
        try {
            articleDao.create(article);
        } catch (SQLException |PersistException e) {
            throw new PersistException("Ошибка при создании статьи в базе данных. " + e.getMessage());
        }
    }

    public void updateArtToDB(Article article) throws PersistException {
        try {
            articleDao.update(article);
        } catch (PersistException e) {
            throw new PersistException("Ошибка при обновления статьи в базе данных. " + e.getMessage());
        }
    }

    public Article getArtByIDFromDB(int articleID) throws PersistException {
        return articleDao.read(articleID);
    }

    public void removeArtFromDB(Article article) throws PersistException {
        try {
            articleDao.delete(article.getId());
            removeArtImgs(article.getArticleImgList());
        } catch (PersistException e) {
            throw new PersistException("Ошибка при удалении статьи в базе данных. " + e.getMessage());
        }
    }

    public void uploadFile(Article article, Part file) throws IOException {
        if (file == null) {
            return;
        }
        String fileName = getFileName(file);
        String pathToFile = loadContextHolder.getPropValues("absolute_art_img_path") + "/" + fileName;
        file.write(pathToFile);

        ArticleImages articleImg = new ArticleImages();
        if (article.getId() != null)
            articleImg.setArticleId(article.getId());
        articleImg.setName(fileName);
        article.getArticleImgList().add(articleImg);
    }

    private String getFileName(Part part) {
        if (part == null)
            return null;
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim()
                        .replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                        .substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }

    public void removeImgFromArticle(Article article, ArticleImages articleImg) throws PersistException {
        article.getArticleImgList().remove(articleImg);
        if(article.getText()!=null)
            article.setText(article.getText().replaceAll(getImgTag(articleImg.getName()), ""));
        removeImgFromIO(articleImg.getName());
    }

    private String getImgTag(String imgName) {
        String imgPath = loadContextHolder.getPropValues("art_img_path") + "/" + imgName;
        String imgTag = "<img src=" + imgPath + ">";
        return imgTag;
    }

    public void pasteImgTagToArtText(Article article, String imgName) {
        String textNew = article.getText() + getImgTag(imgName);
        article.setText(textNew);
    }

    public void rebootCache() throws PersistException {
        try {
            loadContextHolder.rebootCache("db.entity.Article");
        } catch (PersistException e) {
            throw new PersistException("Ошибка обновления кеш данных 'статьи'." + e.getMessage());
        }
    }

    public void removeArtImgs(List<ArticleImages> artImgList) throws PersistException {
        for (ArticleImages artImg : artImgList) {
            String pathToFile = loadContextHolder.getPropValues("absolute_art_img_path")+"/"+artImg.getName();
            File file = new File(pathToFile);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new PersistException("Ошибка удаления файла :" + pathToFile);
                }
            }
        }
    }

    public void removeImgFromIO(String fileName) throws PersistException {
        String pathToFile = loadContextHolder.getPropValues("absolute_art_img_path")+"/"+fileName;
        File file = new File(pathToFile);
        if (file.exists()) {
            if (!file.delete()) {
                throw new PersistException("Ошибка удаления файла :" + pathToFile);
            }
        }
    }
}
