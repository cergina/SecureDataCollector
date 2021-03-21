package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.ArrayList;
import java.util.Dictionary;

public class T_Hash extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "hash" : "hash";

    // Atributes
    private int a_pk;
    private String a_Value;
    private int a_UserID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_VALUE = "Value";
    public static final String DBNAME_USER_ID = "UserID";


    public static T_Hash REFERENCE = new T_Hash();
    public static String[] TABLE_CODENAMES = {
            "Value", "User ID"
    };

    // Constructors
    private T_Hash() {}

    // Creations
    public static T_Hash CreateFromRetrieved(int pk, Dictionary dict) {
        T_Hash temp = new T_Hash();

        temp.a_pk = pk;
        temp.a_Value = (String)dict.get(DBNAME_VALUE);
        temp.a_UserID = (int)dict.get(DBNAME_USER_ID);

        return temp;
    }

    public static T_Hash CreateFromScratch(Dictionary dict) {
        T_Hash temp = new T_Hash();

        temp.a_Value = (String)dict.get(DBNAME_VALUE);
        temp.a_UserID = (int)dict.get(DBNAME_USER_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Hash tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_VALUE, tmp.getA_Value());
        jo.put(DBNAME_USER_ID, tmp.getA_UserID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Value) &&
                Assurance.IsIntOk(a_UserID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Value) &&
                Assurance.IsIntOk(a_UserID);
    }

    @Override
    public String InfoPrintAllColumns() {
        throw new NotImplementedException();
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
    public ArrayList<String> GenerateHtmlTableRow_FromDbRow() {
        ArrayList<String> str = super.GenerateHtmlTableRow_FromDbRow();

        str.add(this.a_Value);
        str.add(Integer.toString(a_UserID));

        return str;
    }

    @Override
    public String[] GetTableCodeNames() {
        return TABLE_CODENAMES;
    }

    // Generic
    @Override
    public String toString() {
        throw new NotImplementedException();
    }

    // Getters

    public int getA_pk() {
        return a_pk;
    }
    public String getA_Value() {
        return a_Value;
    }
    public int getA_UserID() {
        return a_UserID;
    }


}


