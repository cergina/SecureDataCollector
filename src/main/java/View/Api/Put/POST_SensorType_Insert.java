package View.Api.Put;

import Control.ConfigClass;
import Control.Connect.DbProvider;
import Model.Database.Interaction.I_SensorType;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.E_SensorType;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_SensorType_Insert", urlPatterns = POST_SensorType_Insert.SERVLET_URL)
public class POST_SensorType_Insert extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/sensorType-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ConfigClass.PRODUCTION) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL, false);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(E_SensorType.DBNAME_NAME, json.getString(E_SensorType.DBNAME_NAME));
            tmpDict.put(E_SensorType.DBNAME_MEASUREDIN, json.getString(E_SensorType.DBNAME_MEASUREDIN));
            tmpDict.put(E_SensorType.DBNAME_COMMTYPE_ID, json.getInt(E_SensorType.DBNAME_COMMTYPE_ID));

            E_SensorType es = E_SensorType.CreateFromScratch(tmpDict);

            // Insertion
            DbProvider dbProvider = getDb();
            I_SensorType.insert(dbProvider.getConn(), dbProvider.getPs(), es);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
