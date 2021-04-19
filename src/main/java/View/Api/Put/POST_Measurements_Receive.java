package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Support.Measuring.Measurements_Process;
import Model.Database.Support.Measuring.Measurements_SupportedModes;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        try {
            JSONObject jsonMain = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // message type, changes flow of code
            String msgType = jsonMain.getString("messageType");

            // now only supported measurements
            Measurements_SupportedModes mode = Measurements_SupportedModes.valueOfLabel(msgType);

            if (mode == null)
                throw new IOException("MessageType unsupported");

            // process
            DbProvider dbProvider = getDb();
            Measurements_Process.HandleFromPost(dbProvider.getConn(), dbProvider.getPs(), mode, jsonMain);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}