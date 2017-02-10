package controllers.app;

import beans.PageNavigator;
import db.exceptions.PersistException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@ManagedBean(name = "loadContextHolder", eager = true)
@ApplicationScoped
public class LoadContextHolder {
    private Cache cache;
    private PageNavigatorFactory pageNavFactory;
    private ConfigProperties configProp;

    @ManagedProperty("#{dsController}")
    private DataSourceController dsController;

    public LoadContextHolder() throws PersistException {
    }

    @PostConstruct
    private void init() {
        try {
            configProp = new ConfigProperties();
            cache = new Cache();
            pageNavFactory = new PageNavigatorFactory();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    public String getPropValues(String key) {
        return configProp.getPropValues(key);
    }

    public void setDsController(DataSourceController dsController) {
        this.dsController = dsController;
    }

    public List<?> getCacheEntityList(String fullNameEntity) {
        return cache.getCacheDataMap().get(fullNameEntity);
    }

    public Integer getCacheEntityCount(String fullNameEntity) {
        return cache.getCacheCountMap().get(fullNameEntity);
    }

    public PageNavigator getDefaultPageNav(String fullNameEntity) {
        return pageNavFactory.getDefaultPageNav(fullNameEntity);
    }

    public void rebootAllCaches() throws PersistException{
        cache.initCaches("ALL");
    }

    public void rebootCache(String cacheKey) throws PersistException {
            cache.initCaches(cacheKey);
    }

    class ConfigProperties {
        private HashMap<String, String> properties = new HashMap<>();

        public ConfigProperties() {
            initProperties();
        }

        private void initProperties() {
            String propFileName = "config.properties";
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);) {
                Properties prop = new Properties();
                if (inputStream != null) {
                    prop.load(inputStream);
                } else {
                    throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath.");
                }
                for(Map.Entry entry:prop.entrySet()){
                    properties.put((String)entry.getKey(),(String) entry.getValue());
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }

        public String getPropValues(String key){
            return properties.get(key);
        }
    }

    class Cache {
        private HashMap<String, List<?>> cacheDataMap = new HashMap<>();
        private HashMap<String, Integer> cacheCountMap = new HashMap<>();

        public Cache() throws PersistException {
            initCaches("ALL");
        }

        private HashMap<String, List<?>> getCacheDataMap() {
            return cacheDataMap;
        }

        private HashMap<String, Integer> getCacheCountMap() {
            return cacheCountMap;
        }

        private void initCaches(String cacheKey) throws PersistException {
            switch (cacheKey) {
                case "db.entity.Article": {
                    cacheDataMap.put("db.entity.Article", dsController.getArticleDao().getList(new PageNavigator(1, 3, 0)));
                    cacheCountMap.put("db.entity.Article", dsController.getArticleDao().getTotalCount());
                    break;
                }
                case "db.entity.Gallery": {
                    cacheDataMap.put("db.entity.Gallery", dsController.getGalleryDao().getList(new PageNavigator(1, 9, 0)));
                    cacheCountMap.put("db.entity.Gallery", dsController.getGalleryDao().getTotalCount());
                    break;
                }
                case "db.entity.PriceItem": {
                    cacheDataMap.put("db.entity.PriceItem", dsController.getPriceListDao().getList(new PageNavigator(1, 100, 0)));
                    cacheCountMap.put("db.entity.PriceItem", dsController.getPriceListDao().getTotalCount());
                    break;
                }
                case "ALL": {
                    cacheDataMap.clear();
                    cacheDataMap.put("db.entity.Article", dsController.getArticleDao().getList(new PageNavigator(1, 3, 0)));
                    cacheDataMap.put("db.entity.Gallery", dsController.getGalleryDao().getList(new PageNavigator(1, 9, 0)));
                    cacheDataMap.put("db.entity.PriceItem", dsController.getPriceListDao().getList(new PageNavigator(1, 100, 0)));

                    cacheCountMap.clear();
                    cacheCountMap.put("db.entity.Article", dsController.getArticleDao().getTotalCount());
                    cacheCountMap.put("db.entity.Gallery", dsController.getGalleryDao().getTotalCount());
                    cacheCountMap.put("db.entity.PriceItem", dsController.getPriceListDao().getTotalCount());
                    break;
                }
                default:
                    break;
            }
        }
    }

    class PageNavigatorFactory {
        public PageNavigator getDefaultPageNav(String fullNameEntity) {
            switch (fullNameEntity) {
                case "db.entity.Article":
                    return new PageNavigator(1, 3, cache.getCacheCountMap().get(fullNameEntity));
                case "db.entity.Gallery":
                    return new PageNavigator(1, 9, cache.getCacheCountMap().get(fullNameEntity));
                case "db.entity.PriceItem":
                    return new PageNavigator(1, 100, cache.getCacheCountMap().get(fullNameEntity));
                default:
                    return new PageNavigator(1, 1, 0);
            }
        }
    }
}
