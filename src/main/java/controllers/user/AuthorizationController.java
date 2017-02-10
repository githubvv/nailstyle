package controllers.user;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "authorization")
@SessionScoped
public class AuthorizationController implements Serializable {
    private String userName;
    private String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String login() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            if ((request.getUserPrincipal() == null)||(request.getUserPrincipal() != null && !request.getUserPrincipal().getName().equals(userName)))
                    request.login(userName, userPassword);

            if (!request.isUserInRole("admin")) {
                throw new ServletException("This role is deny!");
            }
            return "/faces/admin.xhtml?faces-redirect=true";
        } catch (ServletException ex)
        {
            Logger.getLogger(AuthorizationController.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage("Неправильный логин или пароль!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("author-form", message);
        }
        return null;
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (ServletException e) {
            Logger.getLogger(AuthorizationController.class.getName()).log(Level.SEVERE, null, e);
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return goHome();
    }

    public String goHome() {
        return "/faces/visitor.xhtml?faces-redirect=true";
    }
}
