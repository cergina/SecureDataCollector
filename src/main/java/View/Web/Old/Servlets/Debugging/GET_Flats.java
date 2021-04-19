package View.Web.Old.Servlets.Debugging;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Flat;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Address;
import Model.Database.Tables.T_Flat;
import View.Support.ServletHelper;
import View.Web.Old.Html.CoreBuilder;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GET_Flats", urlPatterns = GET_Flats.SERVLET_URL)
public class GET_Flats extends GET_Database_Interaction {
    public static final String SERVLET_URL =  "/flats";
    public static final String SITE_NAME = "Flats";

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
            List<T_Flat> arr = InteractionWithDatabase.retrieveAll(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), DbEntity.ReturnUnusable(T_Flat.class));
            dbProvider.disconnect();

            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_Flat.REFERENCE);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            // Send back to user
            writer.println(document);
            writer.close();

            CustomLogs.InfoLog("Exited " + SERVLET_URL + ".", true);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
