package controllers.user;

import mailservice.MailService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import java.io.Serializable;

@ManagedBean(name="mailController")
@ViewScoped
public class MailController implements Serializable {
    private String message;
    private String senderName;
    private String senderSurname;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderSurname() {
        return senderSurname;
    }

    public void setSenderSurname(String senderSurname) {
        this.senderSurname = senderSurname;
    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void sendMessage(){
        try {
            String textMessage = (message==null)?"":message + "\n" + "Email: " + email;
            MailService.sendMessage(senderName + " " +senderSurname,textMessage);
            FacesContext.getCurrentInstance().addMessage("Письмо успешно отправлено!", new FacesMessage("Письмо успешно отправлено!"));
            //clear fields
            message = null;
            senderName = null;
            senderSurname = null;
            email = null;
        } catch (MessagingException e) {
            FacesContext.getCurrentInstance().addMessage("mail-message-id", new FacesMessage("Ошибка при отправке сообщения! Попробуйте еще раз."));
        }
    }
}
