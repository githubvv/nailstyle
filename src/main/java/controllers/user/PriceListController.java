package controllers.user;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.entity.PriceItem;
import db.exceptions.PersistException;
import services.PriceListService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

@ManagedBean(name="priceListController")
@ViewScoped
public class PriceListController implements Serializable{
    private List<PriceItem> priceItem;

    @ManagedProperty(value="#{priceListService}")
    private PriceListService priceListService;

    private PageNavigator pageNavigator;

    public PriceListController() {
        this.pageNavigator = ((LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.PriceItem");
    }

    /*GETTERS-SETTERS*/

    public PriceListService getPriceListService() {
        return priceListService;
    }

    public void setPriceListService(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    public List<PriceItem> getPriceItem(){
        List<PriceItem> priceItems = null;
        try {
            priceItems = priceListService.getPriceList(pageNavigator);
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return priceItems;
    }

    public void setPriceItem(List<PriceItem> priceItem) {
        this.priceItem = priceItem;
    }

    public PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

  }
