package Database.Interaction.Servlets.Get;

import Database.Interaction.Entities.Project;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.ServletHelper;
import Database.Tables.T_Project;
import Model.misc.Logs.ConsoleLogging;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "GET_Project_Info", urlPatterns = {"/project-info"})
public class GET_Project_Info extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();
            writer.println("<html><body>");

            T_Project ret_tp = Project.retrieve(conn, ps, rs, Integer.parseInt(req.getParameter("id")));

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

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried

    public void init (){
        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup(DbConfig.DS_CONTEXT_NAME);
            conn = ds.getConnection();
        }
        catch (SQLException se) {
            CustomLogs.DatabaseLog(true,"SQLException: "+se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.DatabaseLog(true,"NamingException: "+ne.getMessage());
        }
    }

    public void destroy() {
        try {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
            if (ctx != null)
                ctx.close();
        }
        catch (SQLException se) {
            CustomLogs.DatabaseLog(true,"SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.DatabaseLog(true,"NamingException: " + ne.getMessage());
        }
    }
}
