package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CommType;
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
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_CommType_Insert", urlPatterns = POST_CommType_Insert.SERVLET_URL)
public class POST_CommType_Insert extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/commType-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(E_CommType.DBNAME_NAME, json.getString(E_CommType.DBNAME_NAME));

            E_CommType ec = E_CommType.CreateFromScratch(tmpDict);

            // Insertion
            DbProvider dbProvider = getDb();
            I_CommType.insert(dbProvider.getConn(), dbProvider.getPs(), ec);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            ServletHelper.SendReturnCode(resp, HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
