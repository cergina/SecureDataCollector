package Database.Interaction.Servlets.Get;

import Database.Enums.E_CommType;
import Database.Interaction.Entities.CommType;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.JSONHelper;
import Database.Support.ServletHelper;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "POST_CommType_Get", urlPatterns = {"/api/commType-byId"})
public class POST_CommType_Get extends HttpServlet {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", "/api/commType-byId");

            E_CommType ret_ect = CommType.retrieve(conn, ps, rs, json.getInt(E_CommType.DBNAME_ID));

            // return
            JSONObject json_toRet = E_CommType.MakeJSONObjectFrom(ret_ect);

            resp.setContentType("application/json; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");

            PrintWriter out = resp.getWriter();
            out.print(json_toRet);
            out.flush();
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
