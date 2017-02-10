package services;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.dao.GalleryDao;
import db.entity.Gallery;
import db.exceptions.PersistException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@ManagedBean(name = "galleryService")
@ApplicationScoped
public class GalleryService {

    @ManagedProperty(value = "#{dsController.galleryDao}")
    private GalleryDao galleryDao;

    @ManagedProperty(value = "#{loadContextHolder}")
    private LoadContextHolder loadContextHolder;

    public GalleryService() { }

    public LoadContextHolder getLoadContextHolder() {
        return loadContextHolder;
    }

    public void setLoadContextHolder(LoadContextHolder loadContextHolder) {
        this.loadContextHolder = loadContextHolder;
    }

    public GalleryDao getGalleryDao() {
        return galleryDao;
    }

    public void setGalleryDao(GalleryDao galleryDao) {
        this.galleryDao = galleryDao;
    }

    public List<Gallery> getPictures(PageNavigator pn) throws PersistException {
        if (pn.getSelectedPageNumber() == 1)
            return (List<Gallery>) loadContextHolder.getCacheEntityList("db.entity.Gallery");
        else return galleryDao.getList(pn);
    }

    public List<Gallery> galleryListDemo(){
        if (loadContextHolder.getCacheEntityList("db.entity.Gallery").size()>4)
            return (List<Gallery>) loadContextHolder.getCacheEntityList("db.entity.Gallery").subList(0,4);
        return (List<Gallery>) loadContextHolder.getCacheEntityList("db.entity.Gallery");
    }

    public void createPicInDB(Gallery pic) throws PersistException {
        try {
            galleryDao.create(pic);
        } catch (SQLException|PersistException e) {
            throw new PersistException("Ошибка создания записи в таблице GALLERY: " +pic.getName() +". " + e.getMessage());
        }
    }

    public void updatePicToDB(Gallery pic) throws PersistException {
        try {
            galleryDao.update(pic);
        } catch (PersistException e) {
            throw new PersistException("Ошибка обновления записи в таблице GALLERY: " +pic.getName() +". " + e.getMessage());
        }
    }

    public Gallery getPicByIDFromDB(int picId) throws PersistException {
        try {
            return galleryDao.read(picId);
        } catch (PersistException e) {
            throw new PersistException("Ошибка получения записи в таблице GALLERY по ID: " +picId +". " + e.getMessage());
        }
    }

    public void removePicFromDB(int picId) throws PersistException {
        try {
            galleryDao.delete(picId);
            /*rebootCache();*/
        } catch (PersistException e) {
            throw new PersistException("Ошибка удаления записи в таблице GALLERY по ID: " +picId +". " + e.getMessage());
        }
    }

    public void uploadFile(String pathToFile, Part file) throws PersistException {
        if (file == null) {
            return;
        }
        try {
            file.write(pathToFile);
        } catch (IOException e) {
            throw new PersistException("Ошибка сохранения файла :" + pathToFile);
        }
    }

    public void removeFile(String pathToFile) throws PersistException {
        File file = new File(pathToFile);
        if (file.exists()) {
            if (!file.delete()) {
                throw new PersistException("Ошибка удаления файла :" + pathToFile);
            }
        }
    }

    public void rebootCache() throws PersistException {
        try {
            loadContextHolder.rebootCache("db.entity.Gallery");
        } catch (PersistException e) {
            throw new PersistException("Ошибка обновления кеш данных 'галерея'." + e.getMessage());
        }
    }

    public String getFullPath() {
       return loadContextHolder.getPropValues("fulls_path");
    }
}
