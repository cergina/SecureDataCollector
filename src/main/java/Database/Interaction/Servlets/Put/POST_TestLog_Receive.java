package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_TestLog;
import Database.Tables.T_User;
import Model.misc.Logs.ConsoleLogging;
import org.json.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Servlet;
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

@WebServlet(name = "POST_TestLog_Receive", urlPatterns = {"/putLog"})
public class POST_TestLog_Receive extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pureBody = ServletHelper.ReturnBodyIfValid(req, "POST", "/putLog");

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_TestLog.DBNAME_EVENT, "/putLog");
            tmpDict.put(T_TestLog.DBNAME_BODY, pureBody);

            T_TestLog tt = T_TestLog.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.TestLogs.insert(conn, ps, tt);
        }
        catch (Exception e) {
            e.printStackTrace();
            ServletHelper.Send417(resp);
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
