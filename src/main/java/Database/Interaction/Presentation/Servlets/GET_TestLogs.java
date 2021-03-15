package Database.Interaction.Presentation.Servlets;

import Database.Interaction.Entities.TestLogs;
import Database.Interaction.Presentation.Html.CoreBuilder;
import Database.Support.CustomLogs;
import Database.Support.GET_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_TestLog;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "GET_TestLogs", urlPatterns = GET_TestLogs.SERVLET_URL)
public class GET_TestLogs extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/test-logs";
    public static final String SITE_NAME = "Test logs";

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
            ArrayList<T_TestLog> arr = TestLogs.retrieveAll(conn, ps, rs);
            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_TestLog.REFERENCE);

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
