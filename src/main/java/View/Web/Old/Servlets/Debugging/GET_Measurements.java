package View.Web.Old.Servlets.Debugging;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Measurements;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_Measurement;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GET_Measurements", urlPatterns = GET_Measurements.SERVLET_URL)
public class GET_Measurements extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/measurements";
    public static final String SITE_NAME = "Measurements";

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
            List<T_Measurement> arr = I_Measurements.retrieveAll(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs());
            dbProvider.disconnect();

            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_Measurement.REFERENCE);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            // Send back to user
            writer.println(document);
            writer.close();

            CustomLogs.InfoLog("Exited " + SERVLET_URL + ".", true);

        } catch (Exception e) {
            ServletHelper.SendReturnCode(resp, HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}

