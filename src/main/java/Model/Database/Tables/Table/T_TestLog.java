package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;
import java.util.List;

public class T_TestLog extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "logs" : "logs";

    // Atributes
    private int a_pk;
    private String a_Event;
    private String a_Body;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_EVENT = "Event";
    public static final String DBNAME_BODY = "Body";

    public static T_TestLog REFERENCE = new T_TestLog();
    public static String[] TABLE_CODENAMES = {
            "Event", "Body"
    };

    // Constructors
    private T_TestLog() {}

    // Creations
    public static T_TestLog CreateFromRetrieved(int pk, Dictionary dict) {
        T_TestLog temp = new T_TestLog();

        temp.a_pk = pk;
        temp.a_Event = (String)dict.get(DBNAME_EVENT);
        temp.a_Body = (String)dict.get(DBNAME_BODY);

        return temp;
    }

    public static T_TestLog CreateFromScratch(Dictionary dict) {
        T_TestLog temp = new T_TestLog();

        temp.a_Event = (String)dict.get(DBNAME_EVENT);
        temp.a_Body = (String)dict.get(DBNAME_BODY);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_TestLog tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_EVENT, tmp.getA_Event());
        jo.put(DBNAME_BODY, tmp.getA_Body());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Event) &&
                Assurance.IsVarcharOk(a_Body);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Event) &&
                Assurance.IsVarcharOk(a_Body);
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

        str.add(this.a_Event);
        str.add(this.a_Body);

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

    public String getA_Event() {
        return a_Event;
    }

    public String getA_Body() {
        return a_Body;
    }
}
