package View.Api.Put;

import Model.Database.Interaction.ControllerUnit;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Table.T_ControllerUnit;
import View.Support.POST_Database_Interaction;
import View.Support.ServletHelper;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_ControllerUnit_Create", urlPatterns = POST_ControllerUnit_Create.SERVLET_URL)
public class POST_ControllerUnit_Create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/controllerUnit-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_ControllerUnit.DBNAME_UID, json.getInt(T_ControllerUnit.DBNAME_UID));
            tmpDict.put(T_ControllerUnit.DBNAME_DIPADDRESS, json.getString(T_ControllerUnit.DBNAME_DIPADDRESS));
            tmpDict.put(T_ControllerUnit.DBNAME_ZWAVE, json.getString(T_ControllerUnit.DBNAME_ZWAVE));
            tmpDict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, json.getInt(T_ControllerUnit.DBNAME_CENTRALUNIT_ID));
            tmpDict.put(T_ControllerUnit.DBNAME_FLAT_ID, json.getInt(T_ControllerUnit.DBNAME_FLAT_ID));

            T_ControllerUnit tcu = T_ControllerUnit.CreateFromScratch(tmpDict);

            // Insertion
            ControllerUnit.insert(dbProvider.getConn(), dbProvider.getPs(), tcu);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
