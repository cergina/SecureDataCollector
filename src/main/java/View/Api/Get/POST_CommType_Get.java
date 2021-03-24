package View.Api.Get;

import Model.Database.Interaction.CommType;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Enum.E_CommType;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_CommType_Get", urlPatterns = POST_CommType_Get.SERVLET_URL)
public class POST_CommType_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/commType-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            E_CommType ret_ect = CommType.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), json.getInt(E_CommType.DBNAME_ID));

            // return
            JSONObject json_toRet = E_CommType.MakeJSONObjectFrom(ret_ect);

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
