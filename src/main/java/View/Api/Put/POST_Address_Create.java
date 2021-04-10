package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Address;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Table.T_Address;
import View.Web.Old.Servlets.POST_Database_Interaction;
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
            DbProvider dbProvider = getDb();
            I_Address.insert(dbProvider.getConn(), dbProvider.getPs(), ta);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
