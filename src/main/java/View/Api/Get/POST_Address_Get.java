package View.Api.Get;

import Model.Database.Interaction.Address;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Table.T_Address;
import View.Support.POST_Database_Interaction;
import View.Support.ServletHelper;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "POST_Address_Get", urlPatterns = POST_Address_Get.SERVLET_URL)
public class POST_Address_Get extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/address-byId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // parse
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            T_Address ret_ta = Address.retrieve(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), json.getInt(T_Address.DBNAME_ID));

            // return
            JSONObject json_toRet = T_Address.MakeJSONObjectFrom(ret_ta);

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

