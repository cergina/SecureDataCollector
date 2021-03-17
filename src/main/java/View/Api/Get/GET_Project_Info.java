package View.Api.Get;

import Model.Database.Interaction.Project;
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

            T_Project ret_tp = Project.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), Integer.parseInt(req.getParameter("id")));

            if (null == ret_tp) {
                writer.println("<p>No project returned!</p>");
            } else {
                writer.println("<p> RETURNED THESE ITEM</p>");

                writer.println("<p>ID: " + ret_tp.getA_pk() + "</p>");
                writer.println("<p>Project Name: " + ret_tp.getA_name() + "</p>");
                writer.println("<p>Created at: " + ret_tp.getA_created() + "</p>");
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
