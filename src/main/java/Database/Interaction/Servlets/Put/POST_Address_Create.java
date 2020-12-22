package Database.Interaction.Servlets.Put;

import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_Address;
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

@WebServlet(name = "POST_Address_Create", urlPatterns = {"/api/address-add"})
public class POST_Address_Create extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", "/api/address-add");

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Address.DBNAME_COUNTRY, json.getString(T_Address.DBNAME_COUNTRY));
            tmpDict.put(T_Address.DBNAME_CITY, json.getString(T_Address.DBNAME_CITY));
            tmpDict.put(T_Address.DBNAME_STREET, json.getString(T_Address.DBNAME_STREET));
            tmpDict.put(T_Address.DBNAME_HOUSENO, json.getString(T_Address.DBNAME_HOUSENO));
            tmpDict.put(T_Address.DBNAME_ZIP, json.getString(T_Address.DBNAME_ZIP));

            T_Address ta = T_Address.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.Address.insert(conn, ps, ta);
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
