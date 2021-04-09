package View.Api.Put;

import Control.Connect.DbProvider;
import Model.Database.Interaction.I_User;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.JSONHelper;
import Model.Database.Tables.Table.T_User;
import View.Support.ServletHelper;
import View.Web.Old.Servlets.POST_Database_Interaction;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "POST_User_Create", urlPatterns = POST_User_Create.SERVLET_URL)
public class POST_User_Create extends POST_Database_Interaction {
    public static final String SERVLET_URL = "/api/user-add";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JSONObject json = JSONHelper.ReturnBodyIfValid(req, "POST", SERVLET_URL);

            // table
            Dictionary tmpDict = new Hashtable();

            tmpDict.put(T_User.DBNAME_BEFORETITLE, json.getString(T_User.DBNAME_BEFORETITLE));
            tmpDict.put(T_User.DBNAME_FIRSTNAME, json.getString(T_User.DBNAME_FIRSTNAME));
            tmpDict.put(T_User.DBNAME_MIDDLENAME, json.getString(T_User.DBNAME_MIDDLENAME));
            tmpDict.put(T_User.DBNAME_LASTNAME, json.getString(T_User.DBNAME_LASTNAME));
            tmpDict.put(T_User.DBNAME_PHONE, json.getString(T_User.DBNAME_PHONE));
            tmpDict.put(T_User.DBNAME_EMAIL, json.getString(T_User.DBNAME_EMAIL));
            tmpDict.put(T_User.DBNAME_PERMANENTRESIDENCE, json.getString(T_User.DBNAME_PERMANENTRESIDENCE));
            tmpDict.put(T_User.DBNAME_BLOCKED, false);


            T_User tu = T_User.CreateFromScratch(tmpDict);

            // Insertion
            DbProvider dbProvider = getDb();
            I_User.insert(dbProvider.getConn(), dbProvider.getPs(), tu);
            dbProvider.disconnect();
        }
        catch (Exception e) {
            ServletHelper.SendReturnCode(resp, HttpServletResponse.SC_NOT_FOUND);

            CustomLogs.Error(e.getMessage());
        }
    }

}
