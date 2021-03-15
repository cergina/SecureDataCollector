package Database.Interaction.Servlets.Put;

import Database.Enums.E_CommType;
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
            Database.Interaction.Entities.CommType.insert(conn, ps, ec);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
