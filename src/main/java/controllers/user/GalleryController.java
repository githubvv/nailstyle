package controllers.user;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.entity.Gallery;
import db.exceptions.PersistException;
import services.GalleryService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "galleryController")
@ViewScoped

public class GalleryController implements Serializable{
   private List<Gallery> galleryList;

    @ManagedProperty(value="#{galleryService}")
    private GalleryService galleryService;

    private PageNavigator pageNavigator;

    public GalleryController() {
        this.pageNavigator = ((LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.Gallery");
    }

    public PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

    public GalleryService getGalleryService() {
        return galleryService;
    }

    public void setGalleryService(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    public String getFullPath(){
        return galleryService.getFullPath();
    }

    public List<Gallery> getGalleryList() {
        List<Gallery> pictures = null;
        try {
            pictures = galleryService.getPictures(pageNavigator);
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return pictures;
    }

    public void setGalleryList(List<Gallery> galleryList) {
        this.galleryList = galleryList;
    }

    public List<Gallery> galleryListDemo() {
        return galleryService.galleryListDemo();
    }

}
