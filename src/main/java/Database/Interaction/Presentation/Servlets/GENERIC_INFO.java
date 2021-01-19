package Database.Interaction.Presentation.Servlets;

import Control.ConfigClass;
import Database.Interaction.Presentation.Html.CoreBuilder;
import Database.Support.ServletHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GENERIC_INFO", urlPatterns = {"/roadmap"})
public class GENERIC_INFO extends HttpServlet {
    public static final String SITE_URL =
            ConfigClass.RUNNING_ON_SERVER ?
                    "/dcs" :
                    "/SecureDataCollector-1.0-SNAPSHOT";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();

            // Base
            StringBuilder document = CoreBuilder.GenerateBaseOfSite("Roadmap");

            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/addresses", "Addresses");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/central-units", "Central Units");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/comm-types", "Types of communication");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/controller-units", "Controller Units");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/flats", "Flats");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/measurements", "Measurements");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/projects", "Projects");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/sensors", "Sensors");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/sensor-types", "Sensor types");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/test-logs", "Test logs");
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + "/users", "Users");

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            writer.println(document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            ServletHelper.Send404(resp);
        }
    }
}
