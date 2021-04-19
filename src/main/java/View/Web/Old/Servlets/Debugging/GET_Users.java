package View.Web.Old.Servlets.Debugging;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_User;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Address;
import Model.Database.Tables.T_User;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
            DbProvider dbProvider = getDb();
            List<T_User> arr = InteractionWithDatabase.retrieveAll(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), DbEntity.ReturnUnusable(T_User.class));
            dbProvider.disconnect();

            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_User.REFERENCE);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            // Send back to user
            writer.println(document);
            writer.close();

            dbProvider.disconnect();
            CustomLogs.InfoLog("Exited " + SERVLET_URL + ".", true);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
