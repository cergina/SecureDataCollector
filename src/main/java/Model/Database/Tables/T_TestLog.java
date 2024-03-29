package Model.Database.Tables;

import Model.Database.Interaction.I_TestLogs;
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
    protected T_TestLog() {}

    // Creations
    public static T_TestLog CreateFromRetrieved(int pk, Dictionary dict) {
        T_TestLog temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_TestLog CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_TestLog CreateBase(Dictionary dict) {
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

    // From DbEntity
    public T_TestLog FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_TestLogs.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isVarcharOk(a_Event) &&
                Assurance.isVarcharOk(a_Body);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_Event) &&
                Assurance.isVarcharOk(a_Body);
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
