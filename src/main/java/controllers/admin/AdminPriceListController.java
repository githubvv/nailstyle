package controllers.admin;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.entity.PriceItem;
import db.exceptions.PersistException;
import services.PriceListService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@ManagedBean(name = "adminPriceListController")
@SessionScoped
public class AdminPriceListController extends AbstractModeController<PriceItem> implements Serializable {
    private PageNavigator pageNavigator;
    List<PriceItem> priceList = null;

    @ManagedProperty(value = "#{priceListService}")
    private PriceListService priceListService;

    public AdminPriceListController() {
        this.pageNavigator = ((LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.PriceItem");
        setGroupChange(true);
    }

    public PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

    public PriceListService getPriceListService() {
        return priceListService;
    }

    public void setPriceListService(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    @Override
    public void setNewObjForSelectedObj() {
        setSelectedObject(new PriceItem());
    }

    public List<PriceItem> getPriceList() {
        try {
            priceList = priceListService.getPriceList(pageNavigator);
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return priceList;
    }

    public void doDBActions() {
        try {
            if (isAddModeView()) {
                priceListService.createPriceItemInDB(getSelectedObject());
            } else if (isEditModeView()) {
                priceListService.updatePriceListToDB(priceList);
            } else if (isRemoveModeView()) {
                priceListService.removePriceItemFromDB(getSelectedObject().getId());
            }
            priceListService.rebootCache();
            cancelModes();
        } catch (SQLException|PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public void cancelActions(){
        try {
            priceListService.rebootCache();
            cancelModes();
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }
}
