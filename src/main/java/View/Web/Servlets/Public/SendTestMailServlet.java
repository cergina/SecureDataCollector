package View.Web.Servlets.Public;

import Control.Communication.MailUtil;
import Control.ConfigClass;
import Model.Database.Support.CustomLogs;
import View.Support.ServletAbstracts.PublicServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SendTestMailServlet", urlPatterns = SendTestMailServlet.SERVLET_URL)
public class SendTestMailServlet extends PublicServlet {
    public static final String SERVLET_URL =  "/send-mail";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.doGet(request, response);

        PrintWriter writer = response.getWriter();

        if (ConfigClass.PRODUCTION_EMAIL_SENDING) {
            try {
                MailUtil.sendMail("mcerget@gmail.com");
                writer.println("Mail sent successfully.");
            } catch (Exception e) {
                CustomLogs.Error(e.getMessage());
                writer.println("Mail failed to be sent.");
            }
        } else {
            writer.println("Mail functionality not turned on.");
        }



        writer.close();
    }
}
