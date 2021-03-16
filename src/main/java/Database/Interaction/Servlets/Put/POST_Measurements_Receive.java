package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Model.Measuring.Measurements_Process;
import Model.Measuring.Measurements_SupportedModes;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "POST_Measurements_Receive", urlPatterns = POST_Measurements_Receive.SERVLET_URL)
public class POST_Measurements_Receive extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/measurements-add";

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
            Measurements_Process.HandleFromPost(conn, ps, mode, jsonMain);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
