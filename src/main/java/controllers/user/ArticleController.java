package controllers.user;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.entity.Article;
import db.exceptions.PersistException;
import services.ArticleService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "articleController")
@ViewScoped
public class ArticleController implements Serializable {
    private Article visitedArticle;
    private boolean showVisitedArticle;

    @ManagedProperty(value="#{articleService}")
    private ArticleService articleService;

    private PageNavigator pageNavigator;

    public ArticleController() throws PersistException {
        this.pageNavigator = ((LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.Article");
        showVisitedArticle = false;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

    public Article getVisitedArticle() {
        return visitedArticle;
    }

    public void setVisitedArticle(Article visitedArticle) {
        this.visitedArticle = visitedArticle;
    }

    public boolean isShowVisitedArticle() {
        return showVisitedArticle;
    }

    public void setShowVisitedArticle(boolean showVisitedArticle) {
        this.showVisitedArticle = showVisitedArticle;
    }

    public String getArticleImgPath(){
       return articleService.getArticleImgPath();
    }

    public List<Article> getArticleList(){
        List<Article> articles=null;
        try {
            articles = articleService.getArticles(pageNavigator);
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return articles;
    }

    public Article getArticleDemo() {
        return articleService.getArticleDemo();
    }

}

