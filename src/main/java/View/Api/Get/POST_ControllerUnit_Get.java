package View.Api.Get;

import Control.Connect.DbProvider;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_ControllerUnit;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_ControllerUnit_Get", urlPatterns = POST_ControllerUnit_Get.SERVLET_URL)
public class POST_ControllerUnit_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/controllerUnit-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            DbProvider dbProvider = getDb();
            T_ControllerUnit ret_tcu = InteractionWithDatabase.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), DbEntity.ReturnUnusable(T_ControllerUnit.class), json.getInt(T_ControllerUnit.DBNAME_ID));
            dbProvider.disconnect();

            // return
            JSONObject json_toRet = T_ControllerUnit.MakeJSONObjectFrom(ret_tcu);

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
