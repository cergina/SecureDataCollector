package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_Flat;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_Flat_Create", urlPatterns = POST_Flat_Create.SERVLET_URL)
public class POST_Flat_Create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/flat-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Flat.DBNAME_APARTMENTNO, json.getString(T_Flat.DBNAME_APARTMENTNO));
            tmpDict.put(T_Flat.DBNAME_ADDRESS_ID, json.getInt(T_Flat.DBNAME_ADDRESS_ID));

            T_Flat tf = T_Flat.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.Flat.insert(conn, ps, tf);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
