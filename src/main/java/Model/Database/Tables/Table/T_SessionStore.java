package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Date;
import java.util.Dictionary;
import java.util.List;

public class T_SessionStore extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "sessionStore" : "sessionstore";

    // Atributes
    private int a_pk;
    private String a_Session;
    private String a_Data;
    private Date a_CreatedAt;
    private Date a_DeletedAt;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_SESSION = "Session";
    public static final String DBNAME_DATA = "Data";
    public static final String DBNAME_CREATEDAT = "CreatedAt";
    public static final String DBNAME_DELETEDAT = "DeletedAt";

    public static T_SessionStore REFERENCE = new T_SessionStore();
    public static String[] TABLE_CODENAMES = {
            "Session", "Data", "Created At", "Deleted At"
    };

    // Constructors
    private T_SessionStore() {}

    // Creations
    public static T_SessionStore CreateFromRetrieved(int pk, Dictionary dict) {
        T_SessionStore temp = new T_SessionStore();

        temp.a_pk = pk;
        temp.a_Session = (String)dict.get(DBNAME_SESSION);
        temp.a_Data = (String)dict.get(DBNAME_DATA);
        temp.a_CreatedAt = (Date)dict.get(DBNAME_CREATEDAT);
        temp.a_DeletedAt = (Date)dict.get(DBNAME_DELETEDAT);

        return temp;
    }

    public static T_SessionStore CreateFromScratch(Dictionary dict) {
        T_SessionStore temp = new T_SessionStore();

        temp.a_Session = (String)dict.get(DBNAME_SESSION);
        temp.a_Data = (String)dict.get(DBNAME_DATA);
        temp.a_CreatedAt = (Date)dict.get(DBNAME_CREATEDAT);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_SessionStore tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_SESSION, tmp.getA_Session());
        jo.put(DBNAME_DATA, tmp.getA_Data());
        jo.put(DBNAME_CREATEDAT, tmp.getA_CreatedAt());

        if (tmp.getA_DeletedAt() != null)
            jo.put(DBNAME_DELETEDAT, tmp.getA_DeletedAt());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isVarcharOk(a_Session) &&
                Assurance.isVarcharOk(a_Data) &&
                Assurance.isDateOk(a_CreatedAt);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_Session) &&
                Assurance.isVarcharOk(a_Data) &&
                Assurance.isDateOk(a_CreatedAt);
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

        str.add(this.a_Session);
        str.add(this.a_Data);
        str.add(this.a_CreatedAt.toString());
        str.add((this.a_DeletedAt == null) ? "" : this.a_DeletedAt.toString());

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

    public String getA_Session() {
        return a_Session;
    }

    public String getA_Data() {
        return a_Data;
    }

    public Date getA_CreatedAt() {
        return a_CreatedAt;
    }

    public Date getA_DeletedAt() {
        return a_DeletedAt;
    }
}
