package mailservice;

import beans.MessageBean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MailService {
    public static void sendMessage(String sender, String textMessage)
            throws MessagingException {
        // Recipient's email ID needs to be mentioned.
        String to = "n******l@gmail.com";// change accordingly

        // Sender's email ID needs to be mentioned
        String from = "n******l@gmail.com";// change accordingly
        final String username = "n******l";// change accordingly
        final String password = "email user password";// change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.mime.charset", "UTF-8");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        message.setHeader("Content-Type", "text/plain;charset=UTF-8");
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));

        // Set Subject: header field
        message.setSubject(sender);

        // Set content: header field
        message.setText(textMessage);
        /* message.setContent(textMessage, "text/plain; charset=windows-1252"); */

        // Send message
        Transport.send(message);

    }

    public static Integer getMessageCount() {

        Authenticator authenticator = null;
        Properties imapProperties = new Properties();
        String gmailUsername = "n******l@gmail.com";
        String gmailPwd = "email user password";

        imapProperties.put("mail.store.protocol", "imap");
        imapProperties.put("mail.imap.host", "imap.gmail.com");
        imapProperties.put("mail.imap.user", gmailUsername);
        imapProperties.put("mail.imap.port", String.valueOf(993));
        imapProperties.put("mail.imap.timeout", "200000");
        imapProperties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        imapProperties.put("mail.imap.socketFactory.fallback", "false");
        imapProperties.put("mail.imap.socketFactory.port", Integer.toString(993));
        authenticator = new PwdAuthenticator(gmailUsername, gmailPwd);
        Integer countMessage = 0;
        try {

            Session mSession = Session.getInstance(imapProperties, authenticator);
            Store mStore = mSession.getStore();
            mStore.connect();
            Folder mInbox = mStore.getFolder("Inbox");
            countMessage = mInbox.getUnreadMessageCount();

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Count message is: " + countMessage);

        return countMessage;
    }

    public static List<MessageBean> getMessages(String mailFlag) {

        List<MessageBean> listMessages = new LinkedList<>();
        if (mailFlag == null)
            return listMessages;

        Flags flag;
        switch (mailFlag) {
            case "SEEN":
                flag = new Flags(Flags.Flag.SEEN);
                break;
            case "ANSWERED":
                flag = new Flags(Flags.Flag.ANSWERED);
                break;
            case "DELETED":
                flag = new Flags(Flags.Flag.DELETED);
                break;
            default:
                flag = new Flags(Flags.Flag.SEEN);
        }

        Authenticator authenticator = null;
        Properties imapProperties = new Properties();
        String gmailUsername = "n******l@gmail.com";
        String gmailPwd = "email user password";
        imapProperties.put("mail.store.protocol", "imap");
        imapProperties.put("mail.imap.host", "imap.gmail.com");
        imapProperties.put("mail.imap.user", gmailUsername);
        imapProperties.put("mail.imap.port", String.valueOf(993));
        imapProperties.put("mail.imap.timeout", "200000");
        imapProperties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        imapProperties.put("mail.imap.socketFactory.fallback", "false");
        imapProperties.put("mail.imap.socketFactory.port", Integer.toString(993));
        authenticator = new PwdAuthenticator(gmailUsername, gmailPwd);

        try {

            Session mSession = Session.getInstance(imapProperties, authenticator);
            Store mStore = mSession.getStore();
            mStore.connect();

            Folder inbox = mStore.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            Message[] messages = inbox.search(new FlagTerm(flag, false));
            listMessages = getPart(messages);

            //Set flag for new messages
            if (mailFlag.equals("SEEN"))
                inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), true);

            inbox.close(false);
            mStore.close();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();

        }
        return listMessages;
    }

    private static LinkedList<MessageBean> getPart(Message[] messages)
            throws MessagingException, IOException {
        LinkedList<MessageBean> listMessages = new LinkedList<>();
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        for (Message inMessage : messages) {
            if (inMessage.isMimeType("text/plain")) {
                MessageBean message = new MessageBean(
                        inMessage.getMessageNumber(),
                        MimeUtility.decodeText(inMessage.getSubject()),
                        inMessage.getFrom()[0].toString(),
                        f.format(inMessage.getSentDate()),
                        (String) inMessage.getContent());
                listMessages.add(message);
            }
        }
        return listMessages;
    }

}

class PwdAuthenticator extends Authenticator {
    String username;
    String password;

    PwdAuthenticator(String name, String passd) {
        username = name;
        password = passd;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}