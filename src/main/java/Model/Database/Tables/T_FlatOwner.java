package Model.Database.Tables;

import Model.Database.Interaction.I_FlatOwner;
import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.List;

public class T_FlatOwner extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "flatOwner" : "flatowner";

    // Atributes
    private int a_pk;
    private String a_BeforeTitle;
    private String a_FirstName;
    private String a_MiddleName;
    private String a_LastName;
    private String a_Phone;
    private String a_Email;
    private String a_Address;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_BEFORETITLE = "BeforeTitle";
    public static final String DBNAME_FIRSTNAME = "FirstName";
    public static final String DBNAME_MIDDLENAME = "MiddleName";
    public static final String DBNAME_LASTNAME = "LastName";
    public static final String DBNAME_PHONE = "Phone";
    public static final String DBNAME_EMAIL = "Email";
    public static final String DBNAME_ADDRESS = "Address";

    public static T_FlatOwner REFERENCE = new T_FlatOwner();
    public static String[] TABLE_CODENAMES = {
            "title", "Name", "Middle-name", "Surname", "Phone", "E-mail", "Address"
    };

    // Constructor
    protected T_FlatOwner() {}

    // Creations
    public static T_FlatOwner CreateFromRetrieved(int pk, Dictionary dict) {
        T_FlatOwner temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_FlatOwner CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_FlatOwner CreateBase(Dictionary dict) {
        T_FlatOwner temp = new T_FlatOwner();

        temp.a_BeforeTitle = (String)dict.get(DBNAME_BEFORETITLE);
        temp.a_FirstName = (String)dict.get(DBNAME_FIRSTNAME);
        temp.a_MiddleName = (String)dict.get(DBNAME_MIDDLENAME);
        temp.a_LastName = (String)dict.get(DBNAME_LASTNAME);
        temp.a_Phone = (String)dict.get(DBNAME_PHONE);
        temp.a_Email = (String)dict.get(DBNAME_EMAIL);
        temp.a_Address = (String)dict.get(DBNAME_ADDRESS);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_FlatOwner tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_BEFORETITLE, tmp.getA_BeforeTitle());
        jo.put(DBNAME_FIRSTNAME, tmp.getA_FirstName());
        jo.put(DBNAME_MIDDLENAME, tmp.getA_MiddleName());
        jo.put(DBNAME_LASTNAME, tmp.getA_LastName());
        jo.put(DBNAME_PHONE, tmp.getA_Phone());
        jo.put(DBNAME_EMAIL, tmp.getA_Email());
        jo.put(DBNAME_ADDRESS, tmp.getA_Address());

        return jo;
    }


    // From DbEntity
    public T_FlatOwner FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_FlatOwner.FillEntity(rs);
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
                Assurance.isVarcharOk(a_LastName) &&
                Assurance.isVarcharOk(a_Phone) &&
                Assurance.isVarcharOk(a_Email) &&
                Assurance.isVarcharOk(a_Address);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_BeforeTitle) &&
                Assurance.isVarcharOk(a_FirstName) &&
                Assurance.isVarcharOk(a_LastName) &&
                Assurance.isVarcharOk(a_Phone) &&
                Assurance.isVarcharOk(a_Email) &&
                Assurance.isVarcharOk(a_Address);
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

        str.add(this.a_BeforeTitle.toString());
        str.add(this.a_FirstName.toString());
        str.add(this.a_MiddleName.toString());
        str.add(this.a_LastName.toString());
        str.add(this.a_Phone.toString());
        str.add(this.a_Email.toString());
        str.add(this.a_Address.toString());

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

    public String getA_Address() {
        return a_Address;
    }
}
