package beans;


import mailservice.MailService;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.mail.MessagingException;

@ManagedBean(name="mailBean")
@RequestScoped
public class MailBean{
    private String recipient;
    private String sender;
    private String message;
    private String statusMessage = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void send() {
        statusMessage = "Message Sent";
        try {
            MailService.sendMessage(sender, message);
        }
        catch(MessagingException ex) {
            statusMessage = ex.getMessage();
        }
    }
}
