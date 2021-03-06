package controllers;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;

public class EmailManager {

    private String from;
    private Session session;

    public EmailManager() {
        this.from= "whereamiplay@gmail.com";
        final String username = "whereamiplay@gmail.com";
        final String password = "bermarthiphi1";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        this.session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private static class SingletonHelper {
        private static final EmailManager INSTANCE = new EmailManager();
    }

    public static EmailManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean send(int option, String to, String headline, String text) { //use option = 0 to send a normal email, option = 1 to send a html email
        try {
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(this.from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(headline);
            if(option==1) {
                message.setText(text, "utf-8", "html");
            }
            else{
                message.setText(text);
            }
            Transport.send(message);
            System.out.println("sent");
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    public String read(String file){
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader("public/emails/" + file +".html"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String text = contentBuilder.toString();
        return text;
    }
}
