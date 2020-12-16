package Database.Interaction.Servlets;

import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_Sensor;
import Model.misc.Logs.ConsoleLogging;
import org.json.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_Sensor_create", urlPatterns = {"/api/sensor-add"})
public class POST_Sensor_create extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", "/api/sensor-add");

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Sensor.DBNAME_INPUT, json.getString(T_Sensor.DBNAME_INPUT));
            tmpDict.put(T_Sensor.DBNAME_NAME, json.getString(T_Sensor.DBNAME_NAME));
            tmpDict.put(T_Sensor.DBNAME_SENSORTYPE_ID, json.getInt(T_Sensor.DBNAME_SENSORTYPE_ID));
            tmpDict.put(T_Sensor.DBNAME_CONTROLLERUNIT_ID, json.getInt(T_Sensor.DBNAME_CONTROLLERUNIT_ID));

            T_Sensor ts = T_Sensor.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.Sensor.insert(conn, ps, ts);
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
            ConsoleLogging.Log("SQLException: "+se.getMessage());
        }
        catch (NamingException ne) {
            ConsoleLogging.Log("NamingException: "+ne.getMessage());
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
