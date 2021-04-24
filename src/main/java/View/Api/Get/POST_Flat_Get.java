package View.Api.Get;

import Control.Connect.DbProvider;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Flat;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_Flat_Get", urlPatterns = POST_Flat_Get.SERVLET_URL)
public class POST_Flat_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/flat-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            DbProvider dbProvider = getDb();
            T_Flat ret_ect = InteractionWithDatabase.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), DbEntity.ReturnUnusable(T_Flat.class), json.getInt(T_Flat.DBNAME_ID));
            dbProvider.disconnect();

            // return
            JSONObject json_toRet = T_Flat.MakeJSONObjectFrom(ret_ect);

            ServletHelper.PrepareResponse_forDoPost_First(resp);
            PrintWriter out = resp.getWriter();

            out.print(json_toRet);
            out.flush();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
