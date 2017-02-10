package services;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.dao.PriceListDao;
import db.entity.PriceItem;
import db.exceptions.PersistException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.sql.SQLException;
import java.util.List;

@ManagedBean(name = "priceListService")
@ApplicationScoped
public class PriceListService {
    @ManagedProperty(value = "#{dsController.priceListDao}")
    private PriceListDao priceListDao;

    @ManagedProperty(value = "#{loadContextHolder}")
    private LoadContextHolder loadContextHolder;

    public PriceListService() { }

    public LoadContextHolder getLoadContextHolder() {
        return loadContextHolder;
    }

    public void setLoadContextHolder(LoadContextHolder loadContextHolder) {
        this.loadContextHolder = loadContextHolder;
    }

    public PriceListDao getPriceListDao() {
        return priceListDao;
    }

    public void setPriceListDao(PriceListDao priceListDao) {
        this.priceListDao = priceListDao;
    }

    public List<PriceItem> getPriceList(PageNavigator pn) throws PersistException {
        if (pn.getSelectedPageNumber() == 1)
            return (List<PriceItem>) loadContextHolder.getCacheEntityList("db.entity.PriceItem");
        else return priceListDao.getList(pn);
    }

    public Integer createPriceItemInDB(PriceItem priceItem) throws PersistException, SQLException {
        try {
            return priceListDao.create(priceItem);
        } catch (PersistException e) {
            throw new PersistException("Ошибка создания записи в таблице PriceList: " +priceItem.getNameService() + e.getMessage());
        }
    }

    public void updatePriceListToDB(List<PriceItem> priceList) throws PersistException {
        try {
            priceListDao.updatePriceList(priceList);
        } catch (PersistException e) {
            throw new PersistException("Ошибка обновления записей в таблице PriceList: " + e.getMessage());
        }
    }

    public PriceItem getPriceItemByIDFromDB(int priceListId) throws PersistException {
        try {
            return priceListDao.read(priceListId);
        } catch (PersistException e) {
            throw new PersistException("Ошибка получения записи в таблице PriceList по ID: " +priceListId +". " + e.getMessage());
        }
    }

    public void removePriceItemFromDB(int priceListId) throws PersistException {
        try {
            priceListDao.delete(priceListId);
        } catch (PersistException e) {
            throw new PersistException("Ошибка удаления записи в таблице PriceList по ID: " +priceListId +". " + e.getMessage());
        }
    }

    public void rebootCache() throws PersistException {
        try {
            loadContextHolder.rebootCache("db.entity.PriceItem");
        } catch (PersistException e) {
            throw new PersistException("Ошибка обновления кеш данных 'прайс-лист'." + e.getMessage());
        }
    }
}
