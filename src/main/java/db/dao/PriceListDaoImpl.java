package db.dao;

import beans.PageNavigator;
import db.entity.PriceItem;
import db.exceptions.PersistException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriceListDaoImpl extends AbstractJdbcDao<PriceItem, PageNavigator> implements PriceListDao {
    public PriceListDaoImpl(DataSource ds) {
        super(ds);
    }

    @Override
    protected String getCreateQuery(PriceItem priceItem) {
        StringBuilder sb = new StringBuilder("INSERT INTO PRICELIST(servicename, price, description, prioritet) VALUES('").append(priceItem.getNameService()).append("',").
                append(priceItem.getPrice()).append(",'").
                append(priceItem.getDescription()).append("',").
                append(priceItem.getPrioritet()).append(");");
        return sb.toString();
    }

    @Override
    protected String getListQuery(PageNavigator pageNav) {
        return "SELECT * FROM PRICELIST ORDER BY PRIORITET;";
    }

    @Override
    protected String getReadQuery(Integer id) {
        StringBuilder sb = new StringBuilder("SELECT * FROM PRICELIST WHERE id=").append(id).append(";");
        return sb.toString();
    }

    @Override
    protected String getUpdateQuery(PriceItem priceItem) {
        StringBuilder sb = new StringBuilder("UPDATE PRICELIST SET servicename='").append(priceItem.getNameService()).append("'").
                append(",price=").append(priceItem.getPrice()).
                append(",description='").append(priceItem.getDescription()).append("'").
                append(",prioritet='").append(priceItem.getPrioritet()).append("'").append(" WHERE id=").append(priceItem.getId()).append(";");
        return sb.toString();
    }

    @Override
    protected String getDeleteQuery(Integer id) {
        StringBuilder sb = new StringBuilder("DELETE PRICELIST WHERE id=").append(id).append(";");
        return sb.toString();
    }

    @Override
    protected String getTotalCountQuery() {
        return "SELECT COUNT(*) FROM PRICELIST";
    }

    @Override
    public List<PriceItem> parseResultSet(ResultSet rs) throws PersistException {
        List<PriceItem> priceItem = new ArrayList<PriceItem>();
        try {
            while (rs.next()) {
                PriceItem price = new PriceItem();
                price.setId(rs.getInt("id"));
                price.setNameService(rs.getString("servicename"));
                price.setPrice(rs.getDouble("price"));
                price.setDescription(rs.getString("description"));
                price.setPrioritet(rs.getInt("prioritet"));
                priceItem.add(price);
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return priceItem;
    }


    public void updatePriceList(List<PriceItem> priceList) throws PersistException {
        StringBuilder sbUpdateQuery = new StringBuilder("");
        for (PriceItem priceItem : priceList) {
            sbUpdateQuery.append(getUpdateQuery(priceItem));
        }
        try (Connection connection = getDs().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sbUpdateQuery.toString())) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistException("Ошибка обновления прайс-листа в базе данных. " + e.getMessage(), e);
        }
    }
}
