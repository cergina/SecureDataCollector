package Database.Interaction.Servlets.Put;

import Database.Enums.E_SensorType;
import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
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
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(E_SensorType.DBNAME_NAME, json.getString(E_SensorType.DBNAME_NAME));
            tmpDict.put(E_SensorType.DBNAME_MEASUREDIN, json.getString(E_SensorType.DBNAME_MEASUREDIN));
            tmpDict.put(E_SensorType.DBNAME_COMMTYPE_ID, json.getInt(E_SensorType.DBNAME_COMMTYPE_ID));

            E_SensorType es = E_SensorType.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.SensorType.insert(conn, ps, es);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
