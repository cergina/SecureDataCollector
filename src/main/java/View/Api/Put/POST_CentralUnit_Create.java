package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_CentralUnit;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Table.T_CentralUnit;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_CentralUnit_Create", urlPatterns = POST_CentralUnit_Create.SERVLET_URL)
public class POST_CentralUnit_Create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/centralUnit-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_CentralUnit.DBNAME_UID, json.getInt(T_CentralUnit.DBNAME_UID));
            tmpDict.put(T_CentralUnit.DBNAME_FRIENDLYNAME, json.getString(T_CentralUnit.DBNAME_FRIENDLYNAME));
            tmpDict.put(T_CentralUnit.DBNAME_SIMNO, json.getString(T_CentralUnit.DBNAME_SIMNO));
            tmpDict.put(T_CentralUnit.DBNAME_IMEI, json.getString(T_CentralUnit.DBNAME_IMEI));
            tmpDict.put(T_CentralUnit.DBNAME_ZWAVE, json.getString(T_CentralUnit.DBNAME_ZWAVE));
            tmpDict.put(T_CentralUnit.DBNAME_PROJECT_ID, json.getInt(T_CentralUnit.DBNAME_PROJECT_ID));
            tmpDict.put(T_CentralUnit.DBNAME_ADDRESS_ID, json.getInt(T_CentralUnit.DBNAME_ADDRESS_ID));

            T_CentralUnit tcu = T_CentralUnit.CreateFromScratch(tmpDict);

            // Insertion
            DbProvider dbProvider = getDb();
            I_CentralUnit.insert(dbProvider.getConn(), dbProvider.getPs(), tcu);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
