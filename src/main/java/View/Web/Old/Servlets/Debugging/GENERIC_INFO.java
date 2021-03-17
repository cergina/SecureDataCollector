package View.Web.Old.Servlets.Debugging;

import Control.ConfigClass;
import Model.Database.Support.CustomLogs;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GENERIC_INFO", urlPatterns = GENERIC_INFO.SERVLET_URL)
public class GENERIC_INFO extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/roadmap";
    public static final String SITE_NAME = "Roadmap";

    public static final String SITE_URL =
            ConfigClass.RUNNING_ON_SERVER ?
                    ConfigClass.URL_BASE_SERVER :
                    ConfigClass.URL_BASE_LOCAL;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CustomLogs.InfoLog("Entered " + SERVLET_URL + ".", true);

            // Base
            req = ServletHelper.ProcessRequest_forDoGet_First(req);
            resp = ServletHelper.PrepareResponse_forDoGet_First(resp);
            PrintWriter writer = resp.getWriter();

            StringBuilder document = CoreBuilder.GenerateBaseOfSite(SITE_NAME);

            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Addresses.SERVLET_URL, GET_Addresses.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_CentralUnits.SERVLET_URL,GET_CentralUnits.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_CommTypes.SERVLET_URL, GET_CommTypes.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_ControllerUnits.SERVLET_URL, GET_ControllerUnits.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Flats.SERVLET_URL, GET_Flats.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Measurements.SERVLET_URL, GET_Measurements.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Projects.SERVLET_URL, GET_Projects.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Sensors.SERVLET_URL, GET_Sensors.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_SensorTypes.SERVLET_URL, GET_SensorTypes.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_TestLogs.SERVLET_URL, GET_TestLogs.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Users.SERVLET_URL, GET_Users.SITE_NAME);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            writer.println(document);
            writer.close();

            CustomLogs.InfoLog("Exited " + SERVLET_URL + ".", true);

        } catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
