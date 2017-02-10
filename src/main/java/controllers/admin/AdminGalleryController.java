package controllers.admin;

import beans.PageNavigator;
import controllers.app.LoadContextHolder;
import db.entity.Gallery;
import db.exceptions.PersistException;
import services.GalleryService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "adminGalleryController")
@SessionScoped
public class AdminGalleryController extends AbstractModeController<Gallery> implements Serializable {
    private Part fileFull;
    private PageNavigator pageNavigator;

    @ManagedProperty(value = "#{galleryService}")
    private GalleryService galleryService;

    @ManagedProperty(value = "#{loadContextHolder}")
    private LoadContextHolder loadContextHolder;

    public AdminGalleryController() {
        this.pageNavigator = ((LoadContextHolder) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("loadContextHolder")).getDefaultPageNav("db.entity.Gallery");
    }

    //GETTERS SETTERS
    public PageNavigator getPageNavigator() {
        return pageNavigator;
    }

    public void setPageNavigator(PageNavigator pageNavigator) {
        this.pageNavigator = pageNavigator;
    }

    public LoadContextHolder getLoadContextHolder() {
        return loadContextHolder;
    }

    public void setLoadContextHolder(LoadContextHolder loadContextHolder) {
        this.loadContextHolder = loadContextHolder;
    }

    public Part getFileFull() {
        return fileFull;
    }

    public void setFileFull(Part fileFull) {
        this.fileFull = fileFull;
    }

    public GalleryService getGalleryService() {
        return galleryService;
    }

    public void setGalleryService(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @Override
    public void setNewObjForSelectedObj() {
        setSelectedObject(new Gallery());
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

    public void doDBActions() {
        setConfirmModeView(false);
        try {
            if (isAddModeView()) {
                if (!filesChoosen()) {
                    FacesContext.getCurrentInstance().addMessage("gallery-form", new FacesMessage("Файлы не выбраны!"));
                    return;
                }
                getSelectedObject().setPath(getFileName(fileFull));

                galleryService.createPicInDB(getSelectedObject());

            } else if (isEditModeView()) {
                if (fileFull != null) {
                    getSelectedObject().setPath(getFileName(fileFull));
                    fileFull = null;
                    galleryService.updatePicToDB(getSelectedObject());
                }
            } else if (isRemoveModeView()) {
                String pathThumbs = null;
                String pathFulls = loadContextHolder.getPropValues("path_fulls") + "/" + getSelectedObject().getPath();
                galleryService.removeFile(pathFulls);
                galleryService.removePicFromDB(getSelectedObject().getId());
            }
            galleryService.rebootCache();
            fileFull=null;
            cancelModes();
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
    }

    public String getFullPath(){
        return galleryService.getFullPath();
    }

    boolean filesChoosen() {
        boolean isChoosen = true;
        if (fileFull == null) {
            FacesContext.getCurrentInstance().addMessage("gallery-form", new FacesMessage("Выбирите файл!"));
            isChoosen = false;
        }
        return isChoosen;
    }

    public void uploadFileFull() throws IOException {
        if (fileFull == null) {
            return;
        }
        String pathToFile = loadContextHolder.getPropValues("absolute_fulls_path") + "/" + getFileName(fileFull);
        try {
            galleryService.uploadFile(pathToFile, fileFull);
        } catch (PersistException e) {
            FacesContext.getCurrentInstance().addMessage("gallery-form", new FacesMessage(e.getMessage()));
        }
    }

    public void cancelGalleryUpdate() throws IOException {
        if (getSelectedObject().getId() == null) {
            String pathToFile = loadContextHolder.getPropValues("absolute_fulls_path") + "/" + getFileName(fileFull);
            try {
                galleryService.removeFile(pathToFile);

            } catch (PersistException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            }
        }
        fileFull=null;
        super.cancelModes();
    }

    public String getFileName(Part file) {
        if (file == null)
            return null;
        for (String cd : file.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim()
                        .replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                        .substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
}
