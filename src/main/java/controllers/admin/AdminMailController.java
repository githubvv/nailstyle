package controllers.admin;

import beans.MessageBean;
import mailservice.MailService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name="adminMailController")
@SessionScoped
public class AdminMailController implements Serializable{
	private String messageCount;
	
	public String getMessageCount() {
		return MailService.getMessageCount().toString();
	}
	
	public List<MessageBean> getMessages(){
		String flag = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("flag");
		return MailService.getMessages(flag);
	}
}
