package main.java.controllers.admin;

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
public class AdminArticleController extends controllers.admin.AbstractModeController<db.entity.Article> implements Serializable {

    private Part file;
    private List<String> fileNames = new ArrayList<>();

    @ManagedProperty(value = "#{articleService}")
    private services.ArticleService articleService;

    private beans.PageNavigator pageNavigator;

    public AdminArticleController() {

        this.pageNavigator = ((controllers.app.LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.Article");
    }

    public beans.PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(beans.PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public services.ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(services.ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void setNewObjForSelectedObj() {
        setSelectedObject(new db.entity.Article());
    }

    public List<db.entity.Article> getArticleList() {
        List<db.entity.Article> articles = null;
        try {
            articles = articleService.getArticles(pageNavigator);
        } catch (db.exceptions.PersistException e) {
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

    public void removeImgFromArticle(db.entity.ArticleImages articleImages) throws db.exceptions.PersistException {
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
            this.pageNavigator = ((controllers.app.LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.Article");
            file = null;
            fileNames.clear();
            cancelModes();
        } catch (db.exceptions.PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void cancelArticleUpdate() {
        if (getSelectedObject().getId() == null) {
            for (String fileName : fileNames) {
                try {
                    articleService.removeImgFromIO(fileName);
                } catch (db.exceptions.PersistException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
                }
            }
        }
        file = null;
        fileNames.clear();
        cancelModes();
    }

}