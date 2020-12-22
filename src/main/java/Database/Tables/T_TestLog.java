package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;

public class T_TestLog implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "logs" : "logs";

    // Atributes
    private int a_pk;
    private String a_Event;
    private String a_Body;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_EVENT = "Event";
    public static final String DBNAME_BODY = "Body";

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

    // Generic
    @Override
    public String toString() {
        throw new NotImplementedException();
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
