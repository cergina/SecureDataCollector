package Database.Interaction.Presentation.Servlets;

import Database.Interaction.Entities.Flat;
import Database.Interaction.Presentation.Html.CoreBuilder;
import Database.Support.DbConfig;
import Database.Support.ServletHelper;
import Database.Tables.T_Flat;
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
import java.util.ArrayList;

@WebServlet(name = "GET_Flats", urlPatterns = {"/flats"})
public class GET_Flats extends HttpServlet {
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

            // Base
            StringBuilder document = CoreBuilder.GenerateBaseOfSite("Flats");

            // Tables
            ArrayList<T_Flat> arr = Flat.retrieveAll(conn, ps, rs);
            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_Flat.REFERENCE);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            // Send back to user
            writer.println(document);
            writer.close();

        } catch (Exception e) {
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
            ConsoleLogging.Log("SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            ConsoleLogging.Log("NamingException: " + ne.getMessage());
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
            ConsoleLogging.Log("SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            ConsoleLogging.Log("NamingException: " + ne.getMessage());
        }
    }
}
