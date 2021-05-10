package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_TestLogs;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Support.Measuring.Measurements_Process;
import Model.Database.Support.Measuring.Measurements_SupportedModes;
import Model.Database.Tables.T_TestLog;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/****
 * Important class made to receive updates via API from outside
 */
@WebServlet(name = "POST_Measurements_Receive", urlPatterns = POST_Measurements_Receive.SERVLET_URL)
public class POST_Measurements_Receive extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/measurements-add";


    /***
     * FROM CENTRAL UNIT or POSTMAN
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // process
        T_TestLog tt = null;
        JSONObject jsonMain = null;

        // Get body, prepare to log it and parse body into json
        try {
            jsonMain = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL, true);

            Dictionary tmpDict = new Hashtable();



            tmpDict.put(T_TestLog.DBNAME_EVENT, "REQUEST at /api/measurements-add");
            tmpDict.put(T_TestLog.DBNAME_BODY, jsonMain.toString());

            tt = T_TestLog.CreateFromScratch(tmpDict);

        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            return;
        }

        DbProvider dbProvider = getDb();

        // Attempt measurement processing and insert
        try {
            // Log what came to server
            I_TestLogs.insert(dbProvider.getConn(), dbProvider.getPs(), tt);

            // message type, changes flow of code
            String msgType = jsonMain.getString("messageType");

            // now only supported measurements
            Measurements_SupportedModes mode = Measurements_SupportedModes.valueOfLabel(msgType);

            if (mode == null)
                throw new IOException("MessageType unsupported");


            Measurements_Process.HandleFromPost(dbProvider.getConn(), dbProvider.getPs(), mode, jsonMain);
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

            CustomLogs.Error(e.getMessage());
        } finally {
            dbProvider.disconnect();
        }
    }

}
