package controllers.admin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="adminNavigationController")
@SessionScoped
public class AdminNavigationController implements Serializable{
	private String pathName = "customizing";

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	
}
