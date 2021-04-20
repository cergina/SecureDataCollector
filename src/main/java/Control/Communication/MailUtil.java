package Control.Communication;

import Control.ConfigClass;
import Model.Database.Support.CustomLogs;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    private final static String SYSTEM_MAIL = "dcs.sonet.slovakia@gmail.com";
    private final static String PASS = "@'DT[3)g(a93sNTp";

    public static void sendRegistrationMail(String recepient, String username, String verCode) throws Exception {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SYSTEM_MAIL, PASS);
            }
        });

        Message msg = prepareRegistrationMessage(session, SYSTEM_MAIL, recepient, username, verCode);

        try {
            Transport.send(msg);
        } catch (Exception m) {
            CustomLogs.Error(m.getMessage());
        }

    }

    public static void sendDefaultMail(String recepient) throws Exception {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SYSTEM_MAIL, PASS);
            }
        });


        Message msg = prepareMessage(session, SYSTEM_MAIL, recepient);

        try {
            Transport.send(msg);
        } catch (Exception m) {
            CustomLogs.Error(m.getMessage());
        }

    }

    private static Message prepareRegistrationMessage(Session session, String projectEmail, String targetEmail, String username, String verCode) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(projectEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetEmail));
            message.setSubject("Registration confirmation");
            message.setContent(generateMailHtml(username, verCode),"text/html; charset=utf-8");
            return message;
        } catch (Exception exception) {
            CustomLogs.Error(exception.getMessage());
        }
        return null;
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

    private static String generateMailHtml(String username, String verCode) {
        String str = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"></head><body><h3>Dear customer " + username;
        str += ", thank you!</h3><p>You've just received this email, because our administrators just registered you" +
                " in our system<br>You can now proceed into registration where you will enter the email you provided us with" +
                "<br>when first discussing our part in our project, create a password that contains at least one<br> uppercase letter, " +
                "one lowercase letter and at least one number and special character.</p><b>" + verCode + "</b>" +
                "<br><a href=\"" + ConfigClass.DEPLOYED_ON_BASE_URL + "/register\">Odkaz na registraciu</a>" +
                "<br><br>If you have any further questions, feel free to contact us at the mail <i>dcs.sonet.slovakia@gmail.com</i><br>" +
                "Remember, do not respond to any emails that would act in our name and ask your password(s) in any matter. " +
                "We don't need them.<br>Have a nice day</body></html>";

        return str;
    }
}
