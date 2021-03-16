package View.Web.Old.Servlets;

import Model.Database.Interaction.User;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.Table.T_User;
import View.Support.GET_Database_Interaction;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "GET_Users", urlPatterns = GET_Users.SERVLET_URL)
public class GET_Users extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/users";
    public static final String SITE_NAME = "Users";

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
            ArrayList<T_User> arr = User.retrieveAll(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs());
            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_User.REFERENCE);

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
