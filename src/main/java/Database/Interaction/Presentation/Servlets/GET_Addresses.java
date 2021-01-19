package Database.Interaction.Presentation.Servlets;

import Database.Interaction.Entities.Address;
import Database.Interaction.Presentation.Html.CoreBuilder;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.ServletHelper;
import Database.Tables.T_Address;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


@WebServlet(name = "GET_Addresses", urlPatterns = GET_Addresses.SERVLET_URL)
public class GET_Addresses extends HttpServlet {
    public static final String SERVLET_URL =  "/addresses";
    public static final String SITE_NAME = "Addresses";

    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    //Logging
    private static final String LOG_FILE = "log4j.properties";
    private static Logger logger = Logger.getLogger(GET_Addresses.class);
    private static Properties properties = new Properties();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            properties.load(new FileInputStream(LOG_FILE));
            PropertyConfigurator.configure(properties);

            logger.info("Entered " + SERVLET_URL + " via doGET()");
            // Base
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();

            StringBuilder document = CoreBuilder.GenerateBaseOfSite(SITE_NAME);

            // Tables
            ArrayList<T_Address> arr = Address.retrieveAll(conn, ps, rs);
            document = CoreBuilder.GenerateDataForPresentation(document, arr, T_Address.REFERENCE);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            // Send back to user
            writer.println(document);
            writer.close();

            logger.info("Exited " + SERVLET_URL + " via doGET()");

        } catch (Exception e) {
            ServletHelper.Send404(resp);

            logger.error(e.getMessage());
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
            CustomLogs.DatabaseLog(true,"SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.DatabaseLog(true,"NamingException: " + ne.getMessage());
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
