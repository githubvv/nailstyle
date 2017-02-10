package beans;

import java.io.Serializable;
 
public class MessageBean implements Serializable { 
    private String subject; 
    private String from; 
    private String dateSent;
    private String content; 
    private int msgId;
 
    public MessageBean(int msgId, String subject, String from, String dateSent, String content) {
        this.subject = subject; 
        this.from = from; 
        this.dateSent = dateSent;
        this.content = content; 
        this.msgId = msgId;
    } 
 
    public String getSubject() { 
        return subject; 
    } 
 
    public void setSubject(String subject) { 
        this.subject = subject; 
    } 
 
    public String getFrom() { 
        return from; 
    } 
 
    public void setFrom(String from) { 
        this.from = from; 
    } 
 
    public String getDateSent() { 
        return dateSent; 
    } 
 
    public void setDateSent(String dateSent) { 
        this.dateSent = dateSent; 
    } 
 
    public String getContent() { 
        return content; 
    } 
 
    public void setContent(String content) { 
        this.content = content; 
    } 
 
    public int getMsgId() {
        return msgId; 
    } 
 
    public void setMsgId(int msgId) { 
        this.msgId = msgId; 
    } 
  
}