package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Project;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.T_Project;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "POST_Project_Create", urlPatterns = POST_Project_Create.SERVLET_URL)
public class POST_Project_Create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/project-add";

    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            T_Project t = T_Project.CreateFromScratch(json.getString(T_Project.DBNAME_NAME));

            DbProvider dbProvider = getDb();
            I_Project.insert(dbProvider.getConn(), dbProvider.getPs(), t);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
