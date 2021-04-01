package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Dictionary;
import java.util.List;

public class T_FlatOwner_flat extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "flatOwner_flat" : "flatowner_flat";

    // Atributes
    private int a_pk;
    private Date a_CreatedAt;
    private Date a_ValidUntil;
    private int a_FlatOwnerID;
    private int a_FlatID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_CREATEDAT = "CreatedAt";
    public static final String DBNAME_VALIDUNTIL = "ValidUntil";
    public static final String DBNAME_FLATOWNERID = "FlatOwnerID";
    public static final String DBNAME_FLATID = "FlatID";

    public static T_FlatOwner_flat REFERENCE = new T_FlatOwner_flat();
    public static String[] TABLE_CODENAMES = {
            "Created at", "Valid until", "ID of flat owner", "ID of flat"
    };

    // Constructors
    private T_FlatOwner_flat() {}

    // Creations
    public static T_FlatOwner_flat CreateFromRetrieved(int pk, Dictionary dict) {
        T_FlatOwner_flat temp = new T_FlatOwner_flat();

        temp.a_pk = pk;
        temp.a_CreatedAt = (Date)dict.get(DBNAME_CREATEDAT);
        temp.a_ValidUntil = (Date)dict.get(DBNAME_VALIDUNTIL);
        temp.a_FlatOwnerID = (int)dict.get(DBNAME_FLATOWNERID);
        temp.a_FlatID = (int)dict.get(DBNAME_FLATID);

        return temp;
    }

    public static T_FlatOwner_flat CreateFromScratch(Dictionary dict) {
        T_FlatOwner_flat temp = new T_FlatOwner_flat();

        temp.a_CreatedAt = (Date)dict.get(DBNAME_CREATEDAT);
        temp.a_FlatOwnerID = (int)dict.get(DBNAME_FLATOWNERID);
        temp.a_FlatID = (int)dict.get(DBNAME_FLATID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_FlatOwner_flat tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_CREATEDAT, tmp.getA_CreatedAt());

        if (tmp.getA_ValidUntil() != null)
            jo.put(DBNAME_VALIDUNTIL, tmp.getA_ValidUntil());

        jo.put(DBNAME_FLATOWNERID, tmp.getA_FlatOwnerID());
        jo.put(DBNAME_FLATID, tmp.getA_FlatID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsDateOk(a_CreatedAt) &&
                Assurance.IsFkOk(a_FlatOwnerID) &&
                Assurance.IsFkOk(a_FlatID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsDateOk(a_CreatedAt) &&
                Assurance.IsFkOk(a_FlatOwnerID) &&
                Assurance.IsFkOk(a_FlatID);
    }

    @Override
    public String InfoPrintAllColumns() {
        return  "id:PK | " +
                "name: varchar | " +
                "CreatedAt: Date | " +
                "ValidUntil: Date | " +
                "FlatOwnerID: FK | " +
                "FlatID: FK";
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

        str.add(this.a_CreatedAt.toString());
        str.add((this.a_ValidUntil == null) ? "" : this.a_ValidUntil.toString());
        str.add(Integer.toString(a_FlatOwnerID));
        str.add(Integer.toString(a_FlatID));

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

    public Date getA_CreatedAt() {
        return a_CreatedAt;
    }

    public Date getA_ValidUntil() {
        return a_ValidUntil;
    }

    public int getA_FlatOwnerID() {
        return a_FlatOwnerID;
    }

    public int getA_FlatID() {
        return a_FlatID;
    }
}
