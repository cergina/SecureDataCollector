package Database.Interaction.Servlets.Put;

import Database.Support.CustomLogs;
import Database.Support.JSONHelper;
import Database.Support.POST_Database_Interaction;
import Database.Support.ServletHelper;
import Database.Tables.T_Address;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_Address_Create", urlPatterns = POST_Address_Create.SERVLET_URL)
public class POST_Address_Create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/address-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Address.DBNAME_COUNTRY, json.getString(T_Address.DBNAME_COUNTRY));
            tmpDict.put(T_Address.DBNAME_CITY, json.getString(T_Address.DBNAME_CITY));
            tmpDict.put(T_Address.DBNAME_STREET, json.getString(T_Address.DBNAME_STREET));
            tmpDict.put(T_Address.DBNAME_HOUSENO, json.getString(T_Address.DBNAME_HOUSENO));
            tmpDict.put(T_Address.DBNAME_ZIP, json.getString(T_Address.DBNAME_ZIP));

            T_Address ta = T_Address.CreateFromScratch(tmpDict);

            // Insertion
            Database.Interaction.Entities.Address.insert(conn, ps, ta);
        }
        catch (Exception e) {
            ServletHelper.Send404(resp);

            CustomLogs.Error(e.getMessage());
        }
    }

}
