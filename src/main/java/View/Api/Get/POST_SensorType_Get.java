package View.Api.Get;

import Control.ConfigClass;
import Control.Connect.DbProvider;
import Model.Database.Interaction.InteractionWithDatabase;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.E_SensorType;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_SensorType_Get", urlPatterns = POST_SensorType_Get.SERVLET_URL)
public class POST_SensorType_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/sensorType-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ConfigClass.PRODUCTION)
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL, false);

            DbProvider dbProvider = getDb();
            E_SensorType ret_ect = InteractionWithDatabase.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), DbEntity.ReturnUnusable(E_SensorType.class), json.getInt(E_SensorType.DBNAME_ID));
            dbProvider.disconnect();

            // return
            JSONObject json_toRet = E_SensorType.MakeJSONObjectFrom(ret_ect);

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
