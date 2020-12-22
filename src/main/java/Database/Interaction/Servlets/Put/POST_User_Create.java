package Database.Interaction.Servlets.Put;

import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_User;
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

@WebServlet(name = "POST_User_Create", urlPatterns = {"/api/user-add"})
public class POST_User_Create extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", "/api/user-add");

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_User.DBNAME_BEFORETITLE, json.getString(T_User.DBNAME_BEFORETITLE));
            tmpDict.put(T_User.DBNAME_FIRSTNAME, json.getString(T_User.DBNAME_FIRSTNAME));
            tmpDict.put(T_User.DBNAME_MIDDLENAME, json.getString(T_User.DBNAME_MIDDLENAME));
            tmpDict.put(T_User.DBNAME_LASTNAME, json.getString(T_User.DBNAME_LASTNAME));
            tmpDict.put(T_User.DBNAME_PHONE, json.getString(T_User.DBNAME_PHONE));
            tmpDict.put(T_User.DBNAME_EMAIL, json.getString(T_User.DBNAME_EMAIL));
            tmpDict.put(T_User.DBNAME_PERMANENTRESIDENCE, json.getString(T_User.DBNAME_PERMANENTRESIDENCE));
            tmpDict.put(T_User.DBNAME_BLOCKED, false);


            T_User tu = T_User.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.User.insert(conn, ps, tu);
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
