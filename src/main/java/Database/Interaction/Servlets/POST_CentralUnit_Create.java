package Database.Interaction.Servlets;

import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_CentralUnit;
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

@WebServlet(name = "POST_CentralUnit_Create", urlPatterns = {"/api/centralUnit-add"})
public class POST_CentralUnit_Create extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", "/api/centralUnit-add");

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_CentralUnit.DBNAME_UID, json.getString(T_CentralUnit.DBNAME_UID));
            tmpDict.put(T_CentralUnit.DBNAME_FRIENDLYNAME, json.getString(T_CentralUnit.DBNAME_FRIENDLYNAME));
            tmpDict.put(T_CentralUnit.DBNAME_SIMNO, json.getString(T_CentralUnit.DBNAME_SIMNO));
            tmpDict.put(T_CentralUnit.DBNAME_IMEI, json.getString(T_CentralUnit.DBNAME_IMEI));
            tmpDict.put(T_CentralUnit.DBNAME_ZWAVE, json.getString(T_CentralUnit.DBNAME_ZWAVE));
            tmpDict.put(T_CentralUnit.DBNAME_PROJECTID, json.get(T_CentralUnit.DBNAME_PROJECTID));
            tmpDict.put(T_CentralUnit.DBNAME_ADDRESSID, json.getString(T_CentralUnit.DBNAME_ADDRESSID));

            T_CentralUnit tcu = T_CentralUnit.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.CentralUnit.insert(conn, ps, tcu);
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
