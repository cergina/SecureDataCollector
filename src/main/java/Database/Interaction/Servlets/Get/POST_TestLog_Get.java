package Database.Interaction.Servlets.Get;

import Database.Interaction.Entities.TestLogs;
import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_TestLog;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_TestLog_Get", urlPatterns = POST_TestLog_Get.SERVLET_URL)
public class POST_TestLog_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/testLog-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            T_TestLog ret_ect = TestLogs.retrieve(conn, ps, rs, json.getInt(T_TestLog.DBNAME_ID));

            // return
            JSONObject json_toRet = T_TestLog.MakeJSONObjectFrom(ret_ect);

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
