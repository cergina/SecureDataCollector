package View.Api.Get;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Project;
import Model.Database.Tables.Table.T_Project;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.GET_Database_Interaction;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GET_Project_Info", urlPatterns = GET_Project_Info.SERVLET_URL)
public class GET_Project_Info extends GET_Database_Interaction {
    public static final String SERVLET_URL = "/project-info";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            // Base
            req = ServletHelper.ProcessRequest_forDoGet_First(req);
            resp = ServletHelper.PrepareResponse_forDoGet_First(resp);
            PrintWriter writer = resp.getWriter();

            writer.println("<html><body>");

            DbProvider dbProvider = getDb();
            T_Project ret_tp = I_Project.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), Integer.parseInt(req.getParameter("id")));
            dbProvider.disconnect();

            if (null == ret_tp) {
                writer.println("<p>No project returned!</p>");
            } else {
                writer.println("<p> RETURNED THESE ITEMS</p>");

                writer.println("<p>ID: " + ret_tp.getA_pk() + "</p>");
                writer.println("<p>Project Name: " + ret_tp.getA_Name() + "</p>");
                writer.println("<p>Created at: " + ret_tp.getA_CreatedAt() + "</p>");
            }
            writer.println("</body></html>");
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            ServletHelper.Send404(resp);
        }
    }

}
