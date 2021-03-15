package Database.Interaction.Servlets.Get;

import Database.Interaction.Entities.Sensor;
import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_Sensor;
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

            T_Sensor ret_ect = Sensor.retrieve(conn, ps, rs, json.getInt(T_Sensor.DBNAME_ID));

            // return
            JSONObject json_toRet = T_Sensor.MakeJSONObjectFrom(ret_ect);

            ServletHelper.PrepareResponse_forDoPost_First(resp);
            PrintWriter out = resp.getWriter();

            out.print(json_toRet);
            out.flush();
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
