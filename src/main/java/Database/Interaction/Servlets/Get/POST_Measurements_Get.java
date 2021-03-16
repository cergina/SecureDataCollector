package Database.Interaction.Servlets.Get;

import Database.Interaction.Entities.Measurements;
import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_Measurement;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_Measurements_Get", urlPatterns = POST_Measurements_Get.SERVLET_URL)
public class POST_Measurements_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/measurement-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            T_Measurement ret_ect = Measurements.retrieve(conn, ps, rs, json.getInt(T_Measurement.DBNAME_ID));

            // return
            JSONObject json_toRet = T_Measurement.MakeJSONObjectFrom(ret_ect);

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
