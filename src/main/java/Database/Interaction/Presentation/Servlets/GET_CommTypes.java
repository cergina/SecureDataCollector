package Database.Interaction.Presentation.Servlets;

import Database.Enums.E_CommType;
import Database.Interaction.Entities.CommType;
import Database.Interaction.Presentation.Html.CoreBuilder;
import Database.Support.CustomLogs;
import Database.Support.GET_Database_Interaction;
import Database.Support.ServletHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "GET_CommTypes", urlPatterns = GET_CommTypes.SERVLET_URL)
public class GET_CommTypes extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/comm-types";
    public static final String SITE_NAME = "Types of communication";

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
            ArrayList<E_CommType> arr = CommType.retrieveAll(conn, ps, rs);
            document = CoreBuilder.GenerateDataForPresentation(document, arr, E_CommType.REFERENCE);

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
