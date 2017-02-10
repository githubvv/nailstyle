package db.dao;

import beans.PageNavigator;
import db.entity.PriceItem;
import db.exceptions.PersistException;

import java.util.List;

public interface PriceListDao extends Crud<PriceItem, PageNavigator> {

    void updatePriceList(List<PriceItem> priceList) throws PersistException;

}
