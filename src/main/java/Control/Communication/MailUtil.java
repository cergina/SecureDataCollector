package Control.Communication;

import Model.Database.Support.CustomLogs;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    private final static String SYSTEM_MAIL = "dcs.sonet.slovakia@gmail.com";
    private final static String PASS = "@'DT[3)g(a93sNTp";

    public static void sendMail(String recepient) throws Exception {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SYSTEM_MAIL, PASS);
            }
        });

        Message msg = prepareMessage(session, SYSTEM_MAIL, recepient);

        Transport.send(msg);
    }

    private static Message  prepareMessage(Session session, String projectEmail, String targetEmail) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(projectEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetEmail));
            message.setSubject("Test message subject");
            message.setText("Hey there\n <br>Welcome at us.");
            return message;
        } catch (Exception exception) {
            CustomLogs.Error(exception.getMessage());
        }

        return null;
    }
}
