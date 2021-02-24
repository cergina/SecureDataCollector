package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
import Database.Tables.T_Flat;
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

@WebServlet(name = "POST_Flat_Create", urlPatterns = POST_Flat_Create.SERVLET_URL)
public class POST_Flat_Create extends HttpServlet {
    public static final String SERVLET_URL = "/api/flat-add";

    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Flat.DBNAME_APARTMENTNO, json.getString(T_Flat.DBNAME_APARTMENTNO));
            tmpDict.put(T_Flat.DBNAME_ADDRESS_ID, json.getInt(T_Flat.DBNAME_ADDRESS_ID));

            T_Flat tf = T_Flat.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.Flat.insert(conn, ps, tf);
        }
        catch (Exception e) {
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
