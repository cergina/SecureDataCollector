package Database.Enums;

import Database.Support.Assurance;
import Database.Support.DBEnum;
import Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;

public class E_CommType implements DBEnum {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "commType" : "commtype";

    // Atributes
    private int a_pk;
    private String a_Name;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_NAME = "Name";

    // Constructors
    private E_CommType() {}

    // Creations
    public static E_CommType CreateFromRetrieved(int pk, Dictionary tmpDict) {
        E_CommType temp = new E_CommType();

        temp.a_pk = pk;
        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);

        return temp;
    }

    public static E_CommType CreateFromScratch(Dictionary tmpDict) {
        E_CommType temp = new E_CommType();

        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(E_CommType tmp) {
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

    // Generic
    @Override
    public String toString() {
        throw new NotImplementedException();
    }

    // Getters

    public int getA_pk() {
        return a_pk;
    }

    public String getA_Name() {
        return a_Name;
    }
}
