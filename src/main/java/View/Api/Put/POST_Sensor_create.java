package View.Api.Put;

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
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_Sensor_create", urlPatterns = POST_Sensor_create.SERVLET_URL)
public class POST_Sensor_create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/sensor-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Sensor.DBNAME_INPUT, json.getString(T_Sensor.DBNAME_INPUT));
            tmpDict.put(T_Sensor.DBNAME_NAME, json.getString(T_Sensor.DBNAME_NAME));
            tmpDict.put(T_Sensor.DBNAME_SENSORTYPE_ID, json.getInt(T_Sensor.DBNAME_SENSORTYPE_ID));
            tmpDict.put(T_Sensor.DBNAME_CONTROLLERUNIT_ID, json.getInt(T_Sensor.DBNAME_CONTROLLERUNIT_ID));

            T_Sensor ts = T_Sensor.CreateFromScratch(tmpDict);

            // Insertion
            I_Sensor.insert(dbProvider.getConn(), dbProvider.getPs(), ts);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
