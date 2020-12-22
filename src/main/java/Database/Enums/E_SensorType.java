package Database.Enums;

import Database.Support.Assurance;
import Database.Support.DBEnum;
import Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;

public class E_SensorType implements DBEnum {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "sensorType" : "sensortype";

    // Atributes
    private int a_pk;
    private String a_Name;
    private String a_MeasuredIn;
    private int a_CommTypeID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_NAME = "Name";
    public static final String DBNAME_MEASUREDIN = "MeasuredIn";
    public static final String DBNAME_COMMTYPE_ID = "CommTypeID";

    // Constructors
    private E_SensorType() {}

    // Creations
    public static E_SensorType CreateFromRetrieved(int pk, Dictionary tmpDict) {
        E_SensorType temp = new E_SensorType();

        temp.a_pk = pk;
        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);
        temp.a_MeasuredIn = (String)tmpDict.get(DBNAME_MEASUREDIN);
        temp.a_CommTypeID = (int)tmpDict.get(DBNAME_COMMTYPE_ID);

        return temp;
    }

    public static E_SensorType CreateFromScratch(Dictionary tmpDict) {
        E_SensorType temp = new E_SensorType();

        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);
        temp.a_MeasuredIn = (String)tmpDict.get(DBNAME_MEASUREDIN);
        temp.a_CommTypeID = (int)tmpDict.get(DBNAME_COMMTYPE_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(E_SensorType tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_NAME, tmp.getA_Name());
        jo.put(DBNAME_MEASUREDIN, tmp.getA_MeasuredIn());
        jo.put(DBNAME_COMMTYPE_ID, tmp.getA_CommTypeID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsEnumTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Name) &&
                Assurance.IsVarcharOk(a_MeasuredIn) &&
                Assurance.IsFkOk(a_CommTypeID);
    }

    @Override
    public boolean WasEnumTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Name) &&
                Assurance.IsVarcharOk(a_MeasuredIn) &&
                Assurance.IsFkOk(a_CommTypeID);
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

    public String getA_MeasuredIn() {
        return a_MeasuredIn;
    }

    public int getA_CommTypeID() {
        return a_CommTypeID;
    }
}
