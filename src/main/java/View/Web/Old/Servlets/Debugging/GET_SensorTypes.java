package View.Web.Old.Servlets.Debugging;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_SensorType;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Enum.E_SensorType;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GET_SensorTypes", urlPatterns = GET_SensorTypes.SERVLET_URL)
public class GET_SensorTypes extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/sensor-types";
    public static final String SITE_NAME = "Sensor types";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CustomLogs.InfoLog("Entered " + SERVLET_URL + ".", true);

            // Base
            req = ServletHelper.ProcessRequest_forDoGet_First(req);
            resp = ServletHelper.PrepareResponse_forDoGet_First(resp);
            PrintWriter writer = resp.getWriter();

            StringBuilder document = CoreBuilder.GenerateBaseOfSite(SITE_NAME);

            // Tables
            DbProvider dbProvider = getDb();
            List<E_SensorType> arr = I_SensorType.retrieveAll(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs());
            dbProvider.disconnect();

            document = CoreBuilder.GenerateDataForPresentation(document, arr, E_SensorType.REFERENCE);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            // Send back to user
            writer.println(document);
            writer.close();

            CustomLogs.InfoLog("Exited " + SERVLET_URL + ".", true);

        } catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
