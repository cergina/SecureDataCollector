package Model.Database.Tables;

import Model.Database.Interaction.I_Hash;
import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.List;

public class T_Hash extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "hash" : "hash";

    // Atributes
    private int a_pk;
    private String a_Value;
    private int a_UserID;
    private byte[] a_NaCl;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_VALUE = "Value";
    public static final String DBNAME_USER_ID = "UserID";
    public static final String DBNAME_NACL = "NaCl";


    public static T_Hash REFERENCE = new T_Hash();
    public static String[] TABLE_CODENAMES = {
            "Value", "User ID"
    };

    // Constructors
    protected T_Hash() {}

    // Creations
    public static T_Hash CreateFromRetrieved(int pk, Dictionary dict) {
        T_Hash temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_Hash CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_Hash CreateBase(Dictionary dict) {
        T_Hash temp = new T_Hash();

        temp.a_Value = (String)dict.get(DBNAME_VALUE);
        temp.a_UserID = (int)dict.get(DBNAME_USER_ID);
        temp.a_NaCl = (byte[]) dict.get(DBNAME_NACL);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Hash tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_VALUE, tmp.getA_Value());
        jo.put(DBNAME_USER_ID, tmp.getA_UserID());
        // dont put salt here

        return jo;
    }

    // From DbEntity
    public T_Hash FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_Hash.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }



    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isVarcharOk(a_Value) &&
                Assurance.isFkOk(a_UserID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_Value) &&
                Assurance.isFkOk(a_UserID);
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
    public List<String> GenerateHtmlTableRow_FromDbRow() {
        List<String> str = super.GenerateHtmlTableRow_FromDbRow();

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
        StringBuilder str = new StringBuilder("[");

        return str.toString();
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
    public byte[] getA_NaCl() {return a_NaCl; }


}


