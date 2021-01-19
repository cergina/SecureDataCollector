package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_ControllerUnit;
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

@WebServlet(name = "POST_ControllerUnit_Create", urlPatterns = {"/api/controllerUnit-add"})
public class POST_ControllerUnit_Create extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", "/api/controllerUnit-add");

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_ControllerUnit.DBNAME_UID, json.getInt(T_ControllerUnit.DBNAME_UID));
            tmpDict.put(T_ControllerUnit.DBNAME_DIPADDRESS, json.getString(T_ControllerUnit.DBNAME_DIPADDRESS));
            tmpDict.put(T_ControllerUnit.DBNAME_ZWAVE, json.getString(T_ControllerUnit.DBNAME_ZWAVE));
            tmpDict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, json.getInt(T_ControllerUnit.DBNAME_CENTRALUNIT_ID));
            tmpDict.put(T_ControllerUnit.DBNAME_FLAT_ID, json.getInt(T_ControllerUnit.DBNAME_FLAT_ID));

            T_ControllerUnit tcu = T_ControllerUnit.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.ControllerUnit.insert(conn, ps, tcu);
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
