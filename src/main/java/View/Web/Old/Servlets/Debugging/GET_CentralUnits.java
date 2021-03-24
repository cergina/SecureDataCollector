package View.Web.Old.Servlets.Debugging;

import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_CentralUnit;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "GET_CentralUnits", urlPatterns = GET_CentralUnits.SERVLET_URL)
public class GET_CentralUnits extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/central-units";
    public static final String SITE_NAME = "Central Units";

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
            ArrayList<T_CentralUnit> arr = I_CentralUnit.retrieveAll(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs());
            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_CentralUnit.REFERENCE);

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
