package controllers.user;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name="navigation")
@ViewScoped
public class NavigationController implements Serializable {
    private String face;

    public void setFace(String face) {
        this.face = face;
    }

    public String getFace() {
        return face;
    }

}
