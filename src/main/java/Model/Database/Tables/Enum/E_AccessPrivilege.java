package Model.Database.Tables.Enum;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBEnum;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;
import java.util.List;

public class E_AccessPrivilege extends DbEntity implements DBEnum, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "accessPrivilege" : "accessprivilege";

    // Atributes
    private int a_pk;
    private String a_Name;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_NAME = "Name";

    public static E_AccessPrivilege REFERENCE = new E_AccessPrivilege();
    public static String[] TABLE_CODENAMES = {
            "Name"
    };

    // Constructors
    private E_AccessPrivilege() {}

    // Creations
    public static E_AccessPrivilege CreateFromRetrieved(int pk, Dictionary tmpDict) {
        E_AccessPrivilege temp = new E_AccessPrivilege();

        temp.a_pk = pk;
        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);

        return temp;
    }

    public static E_AccessPrivilege CreateFromScratch(Dictionary tmpDict) {
        E_AccessPrivilege temp = new E_AccessPrivilege();

        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(E_AccessPrivilege tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_NAME, tmp.getA_Name());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsEnumTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Name);
    }

    @Override
    public boolean WasEnumTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Name);
    }

    @Override
    public String PrintInfoAboutEnum() {
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

        str.add(this.a_Name);

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

    public String getA_Name() {
        return a_Name;
    }
}
