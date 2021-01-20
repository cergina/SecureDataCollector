package Database.Interaction.Presentation.Servlets;

import Control.ConfigClass;
import Database.Interaction.Presentation.Html.CoreBuilder;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.ServletHelper;

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

@WebServlet(name = "GENERIC_INFO", urlPatterns = GENERIC_INFO.SERVLET_URL)
public class GENERIC_INFO extends HttpServlet {
    public static final String SERVLET_URL =  "/roadmap";
    public static final String SITE_NAME = "Roadmap";

    public static final String SITE_URL =
            ConfigClass.RUNNING_ON_SERVER ?
                    "/dcs" :
                    "/SecureDataCollector-1.0-SNAPSHOT";

    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CustomLogs.InfoLog("Entered " + SERVLET_URL + ".", true);

            // Base
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();

            StringBuilder document = CoreBuilder.GenerateBaseOfSite(SITE_NAME);

            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Addresses.SERVLET_URL, GET_Addresses.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_CentralUnits.SERVLET_URL,GET_CentralUnits.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_CommTypes.SERVLET_URL, GET_CommTypes.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_ControllerUnits.SERVLET_URL, GET_ControllerUnits.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Flats.SERVLET_URL, GET_Flats.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Measurements.SERVLET_URL, GET_Measurements.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Projects.SERVLET_URL, GET_Projects.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Sensors.SERVLET_URL, GET_Sensors.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_SensorTypes.SERVLET_URL, GET_SensorTypes.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_TestLogs.SERVLET_URL, GET_TestLogs.SITE_NAME);
            document = CoreBuilder.AddLinkWithName(document,SITE_URL + GET_Users.SERVLET_URL, GET_Users.SITE_NAME);

            // Finalize
            document = CoreBuilder.FinalizeSite(document);

            writer.println(document);
            writer.close();

            CustomLogs.InfoLog("Exited " + SERVLET_URL + ".", true);

        } catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
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
            CustomLogs.Error("SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.Error("NamingException: " + ne.getMessage());
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

            CustomLogs.Error("SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.Error("NamingException: " + ne.getMessage());
        }
    }
}
