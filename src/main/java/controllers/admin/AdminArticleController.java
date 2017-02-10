package controllers.admin;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.entity.Article;
import db.entity.ArticleImages;
import db.exceptions.PersistException;
import services.ArticleService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "adminArticleController")
@SessionScoped
public class AdminArticleController extends AbstractModeController<Article> implements Serializable {

    private Part file;
    private List<String> fileNames = new ArrayList<>();

    @ManagedProperty(value = "#{articleService}")
    private ArticleService articleService;

    private PageNavigator pageNavigator;

    public AdminArticleController() {

        this.pageNavigator = ((LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.Article");
    }

    public PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void setNewObjForSelectedObj() {
        setSelectedObject(new Article());
    }

    public List<Article> getArticleList() {
        List<Article> articles = null;
        try {
            articles = articleService.getArticles(pageNavigator);
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return articles;
    }

    public void uploadFile() {
        try {
            articleService.uploadFile(getSelectedObject(), file);
            fileNames.add(file.getSubmittedFileName());
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage("Error file uploading! " + e.getMessage(), null);
        }
    }

    public void removeImgFromArticle(ArticleImages articleImages) throws PersistException {
        if (articleImages != null)
            articleService.removeImgFromArticle(getSelectedObject(), articleImages);
    }

    public void pasteImgTag(String imgName) {
        articleService.pasteImgTagToArtText(getSelectedObject(), imgName);
    }

    public String getArticleImgPath() {
        return articleService.getArticleImgPath();
    }

    public void doDBActions() {
        try {
            if (isAddModeView()) {
                articleService.createArtInDB(getSelectedObject());
            } else if (isEditModeView()) {
                articleService.updateArtToDB(getSelectedObject());
            } else if (isRemoveModeView()) {
                articleService.removeArtFromDB(getSelectedObject());
            }
            articleService.rebootCache();
            file = null;
            fileNames.clear();
            cancelModes();
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void cancelArticleUpdate() {
        if (getSelectedObject().getId() == null) {
            for (String fileName : fileNames) {
                try {
                    articleService.removeImgFromIO(fileName);
                } catch (PersistException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
                }
            }
        }
        file = null;
        fileNames.clear();
        cancelModes();
    }

}