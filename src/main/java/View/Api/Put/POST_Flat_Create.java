package View.Api.Put;

import Control.ConfigClass;
import Control.Connect.DbProvider;
import Model.Database.Interaction.I_Flat;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.T_Flat;
import View.Web.Old.Servlets.POST_Database_Interaction;
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
        if (ConfigClass.PRODUCTION) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL, false);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_Flat.DBNAME_APARTMENTNO, json.getString(T_Flat.DBNAME_APARTMENTNO));
            tmpDict.put(T_Flat.DBNAME_BUILDING_ID, json.getInt(T_Flat.DBNAME_BUILDING_ID));

            T_Flat tf = T_Flat.CreateFromScratch(tmpDict);

            // Insertion
            DbProvider dbProvider = getDb();
            I_Flat.insert(dbProvider.getConn(), dbProvider.getPs(), tf);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
