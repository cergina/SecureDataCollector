package Model.Database.Tables;


import Model.Database.Interaction.I_ProjectUser;
import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.List;

public class T_Project_user extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "project_user" : "project_user";

    // Atributes
    private int a_pk;
    private int a_ProjectID;
    private int a_UserID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_PROJECTID = "ProjectID";
    public static final String DBNAME_USERID = "UserID";

    public static T_Project_user REFERENCE = new T_Project_user();
    public static String[] TABLE_CODENAMES = {
            "Project ID", "User ID"
    };

    // Constructors
    protected T_Project_user() {}

    // Creations

    public static T_Project_user CreateFromRetrieved(int pk, Dictionary dict) {
        T_Project_user temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_Project_user CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_Project_user CreateBase(Dictionary dict) {
        T_Project_user temp = new T_Project_user();

        temp.a_ProjectID = (int)dict.get(DBNAME_PROJECTID);
        temp.a_UserID = (int)dict.get(DBNAME_USERID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Project_user tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_PROJECTID, tmp.getA_ProjectID());
        jo.put(DBNAME_USERID, tmp.getA_UserID());

        return jo;
    }


    // From DbEntity
    public T_Project_user FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_ProjectUser.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific

    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isFkOk(a_ProjectID) &&
                Assurance.isFkOk(a_UserID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isFkOk(a_ProjectID) &&
                Assurance.isFkOk(a_UserID);
    }


    @Override
    public String InfoPrintAllColumns() {
        return  "id:PK | ";
    }

    @Override
    public String ReturnDBNamesInHtmlRow() {
        String documentPart = "";
        documentPart += "<tr>";

        for (String str:TABLE_CODENAMES
        ) {
            documentPart += "<th>" + str + "</th>";
        }

        documentPart += "<th></th>";
        documentPart += "</tr>";

        return documentPart;
    }

    // For HTML purposes
    @Override
    public List<String> GenerateHtmlTableRow_FromDbRow() {
        List<String> str = super.GenerateHtmlTableRow_FromDbRow();

        str.add(Integer.toString(this.a_pk));
        str.add(Integer.toString(this.a_ProjectID));
        str.add(Integer.toString(this.a_UserID));

        return str;
    }

    @Override
    public String[] GetTableCodeNames() {
        return TABLE_CODENAMES;
    }


    // Generic
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");


        return str.toString();
    }

    // Getters

    public int getA_pk() {
        return a_pk;
    }

    public int getA_ProjectID() {
        return a_ProjectID;
    }

    public int getA_UserID() {
        return a_UserID;
    }
}
