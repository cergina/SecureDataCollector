package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_TestLog;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_TestLog_Receive", urlPatterns = POST_TestLog_Receive.SERVLET_URL)
public class POST_TestLog_Receive extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/putLog";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pureBody = ServletHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_TestLog.DBNAME_EVENT, "/putLog");
            tmpDict.put(T_TestLog.DBNAME_BODY, pureBody);

            T_TestLog tt = T_TestLog.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.TestLogs.insert(conn, ps, tt);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
