package View.Api.Get;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Sensor;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Table.T_Sensor;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_Sensor_Get", urlPatterns = POST_Sensor_Get.SERVLET_URL)
public class POST_Sensor_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/sensor-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            DbProvider dbProvider = getDb();
            T_Sensor ret_ect = I_Sensor.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), json.getInt(T_Sensor.DBNAME_ID));
            dbProvider.disconnect();

            // return
            JSONObject json_toRet = T_Sensor.MakeJSONObjectFrom(ret_ect);

            ServletHelper.PrepareResponse_forDoPost_First(resp);
            PrintWriter out = resp.getWriter();

            out.print(json_toRet);
            out.flush();
        }
        catch (Exception e) {
            ServletHelper.SendReturnCode(resp, HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
