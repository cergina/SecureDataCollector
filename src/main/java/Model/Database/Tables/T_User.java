package Model.Database.Tables;

import Model.Database.Interaction.I_User;
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

public class T_User extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "user" : "user";

    // Atributes
    private int a_pk;
    private String a_BeforeTitle;
    private String a_FirstName;
    private String a_MiddleName;
    private String a_LastName;
    private String a_Phone;
    private String a_Email;
    private String a_PermanentResidence;
    private boolean a_Blocked;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_BEFORETITLE = "BeforeTitle";
    public static final String DBNAME_FIRSTNAME = "FirstName";
    public static final String DBNAME_MIDDLENAME = "MiddleName";
    public static final String DBNAME_LASTNAME = "LastName";
    public static final String DBNAME_PHONE = "Phone";
    public static final String DBNAME_EMAIL = "Email";
    public static final String DBNAME_PERMANENTRESIDENCE = "PermanentResidence";
    public static final String DBNAME_BLOCKED = "Blocked";

    public static T_User REFERENCE = new T_User();
    public static String[] TABLE_CODENAMES = {
            "Title before Name", "Name", "Middle name", "Surname", "Phone", "E-Mail", "Permanent residence address"
    };

    // Constructors
    protected T_User() {}

    // Creations
    public static T_User CreateFromRetrieved(int pk, Dictionary dict) {
        T_User temp = CreateBase(dict);

        temp.a_pk = pk;
        temp.a_Blocked = ((int)dict.get(DBNAME_BLOCKED) == 1);

        return temp;
    }

    public static T_User CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_User CreateBase(Dictionary dict) {
        T_User temp = new T_User();

        temp.a_BeforeTitle = (String)dict.get(DBNAME_BEFORETITLE);
        temp.a_FirstName = (String)dict.get(DBNAME_FIRSTNAME);
        temp.a_MiddleName = (String)dict.get(DBNAME_MIDDLENAME);
        temp.a_LastName = (String)dict.get(DBNAME_LASTNAME);
        temp.a_Phone = (String)dict.get(DBNAME_PHONE);
        temp.a_Email = (String)dict.get(DBNAME_EMAIL);
        temp.a_PermanentResidence = (String)dict.get(DBNAME_PERMANENTRESIDENCE);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_User tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_BEFORETITLE, tmp.getA_BeforeTitle());
        jo.put(DBNAME_FIRSTNAME, tmp.getA_FirstName());
        jo.put(DBNAME_MIDDLENAME, tmp.getA_MiddleName());
        jo.put(DBNAME_LASTNAME, tmp.getA_LastName());
        jo.put(DBNAME_PHONE, tmp.getA_Phone());
        jo.put(DBNAME_EMAIL, tmp.getA_Email());
        jo.put(DBNAME_PERMANENTRESIDENCE, tmp.getA_PermanentResidence());
        jo.put(DBNAME_BLOCKED, tmp.isA_Blocked_Numerical());

        return jo;
    }

    // From DbEntity
    public T_User FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_User.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isVarcharOk(a_BeforeTitle) &&
                Assurance.isVarcharOk(a_FirstName) &&
                Assurance.isVarcharOk(a_MiddleName) &&
                Assurance.isVarcharOk(a_LastName) &&
                Assurance.isVarcharOk(a_Phone) &&
                Assurance.isVarcharOk(a_Email) &&
                Assurance.isVarcharOk(a_PermanentResidence);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_BeforeTitle) &&
                Assurance.isVarcharOk(a_FirstName) &&
                Assurance.isVarcharOk(a_MiddleName) &&
                Assurance.isVarcharOk(a_LastName) &&
                Assurance.isVarcharOk(a_Phone) &&
                Assurance.isVarcharOk(a_Email) &&
                Assurance.isVarcharOk(a_PermanentResidence);
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

        str.add(this.a_BeforeTitle);
        str.add(this.a_FirstName);
        str.add(this.a_MiddleName);
        str.add(this.a_LastName);
        str.add(this.a_Phone);
        str.add(this.a_Email);
        str.add(this.a_PermanentResidence);

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

    public String getA_BeforeTitle() {
        return a_BeforeTitle;
    }

    public String getA_FirstName() {
        return a_FirstName;
    }

    public String getA_MiddleName() {
        return a_MiddleName;
    }

    public String getA_LastName() {
        return a_LastName;
    }

    public String getA_Phone() {
        return a_Phone;
    }

    public String getA_Email() {
        return a_Email;
    }

    public String getA_PermanentResidence() {
        return a_PermanentResidence;
    }

    public boolean isA_Blocked() { return a_Blocked; }

    public int isA_Blocked_Numerical() {
        return (a_Blocked == true) ? 1 : 0;
    }
}
