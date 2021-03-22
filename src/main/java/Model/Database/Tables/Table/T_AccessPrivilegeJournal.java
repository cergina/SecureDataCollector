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

public class T_AccessPrivilegeJournal extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "useraccessprivilege_journal" : "useraccessprivilege_journal";

    // Atributes
    private int a_pk;
    private java.sql.Date a_CreatedAt;
    private java.sql.Date a_DeletedAt;
    private int a_UserID;
    private int a_AccessPrivilegeID;
    private int a_CreatedByUserID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_CREATED_AT = "CreatedAt";
    public static final String DBNAME_DELETED_AT = "DeletedAt";
    public static final String DBNAME_USER_ID = "UserID";
    public static final String DBNAME_ACCESS_PRIVILEGE_ID = "AccessPrivilegeID";
    public static final String DBNAME_CREATED_BY_USER_ID = "CreatedByUserID";

    public static T_AccessPrivilegeJournal REFERENCE = new T_AccessPrivilegeJournal();
    public static String[] TABLE_CODENAMES = {
            "Created At", "Deleted At", "User ID", "Access Privilege ID", "CreatedBy User ID"
    };

    // Constructors
    private T_AccessPrivilegeJournal() {}

    // Creations
    public static T_AccessPrivilegeJournal CreateFromRetrieved(int pk, Dictionary dict) {
        T_AccessPrivilegeJournal temp = new T_AccessPrivilegeJournal();

        temp.a_pk = pk;
        temp.a_CreatedAt = (java.sql.Date)dict.get(DBNAME_CREATED_AT);
        temp.a_DeletedAt = (java.sql.Date)dict.get(DBNAME_DELETED_AT);
        temp.a_UserID = (int)dict.get(DBNAME_USER_ID);
        temp.a_AccessPrivilegeID = (int)dict.get(DBNAME_ACCESS_PRIVILEGE_ID);
        temp.a_CreatedByUserID = (int)dict.get(DBNAME_CREATED_BY_USER_ID);

        return temp;
    }

    public static T_AccessPrivilegeJournal CreateFromScratch(Dictionary dict) {
        T_AccessPrivilegeJournal temp = new T_AccessPrivilegeJournal();

        temp.a_CreatedAt = (java.sql.Date)dict.get(DBNAME_CREATED_AT);
        temp.a_UserID = (int)dict.get(DBNAME_USER_ID);
        temp.a_AccessPrivilegeID = (int)dict.get(DBNAME_ACCESS_PRIVILEGE_ID);
        temp.a_CreatedByUserID = (int)dict.get(DBNAME_CREATED_BY_USER_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_AccessPrivilegeJournal tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_CREATED_AT, tmp.getA_CreatedAt());
        jo.put(DBNAME_DELETED_AT, tmp.getA_DeletedAt());
        jo.put(DBNAME_USER_ID, tmp.getA_UserID());
        jo.put(DBNAME_ACCESS_PRIVILEGE_ID, tmp.getA_AccessPrivilegeID());
        jo.put(DBNAME_CREATED_BY_USER_ID, tmp.getA_CreatedByUserID());

        return jo;
    }



    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsDateOk(a_CreatedAt) &&
                Assurance.IsIntOk(a_UserID) &&
                Assurance.IsIntOk(a_AccessPrivilegeID) &&
                Assurance.IsIntOk(a_CreatedByUserID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsDateOk(a_CreatedAt) &&
                Assurance.IsIntOk(a_UserID) &&
                Assurance.IsIntOk(a_AccessPrivilegeID) &&
                Assurance.IsIntOk(a_CreatedByUserID);
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

        str.add(a_CreatedAt.toString());
        str.add(a_DeletedAt.toString());
        str.add(Integer.toString(a_UserID));
        str.add(Integer.toString(a_AccessPrivilegeID));
        str.add(Integer.toString(a_CreatedByUserID));

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
    public java.sql.Date getA_CreatedAt() {
        return a_CreatedAt;
    }
    public java.sql.Date getA_DeletedAt() {
        return a_DeletedAt;
    }
    public int getA_UserID() {
        return a_UserID;
    }
    public int getA_AccessPrivilegeID() {
        return a_AccessPrivilegeID;
    }
    public int getA_CreatedByUserID() {
        return a_CreatedByUserID;
    }

}