package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Date;
import java.util.Dictionary;

public class T_Measurement implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "measurement" : "measurement";

    // Atributes
    private int a_pk;
    private int a_Value;
    private int a_MeasuredIncrement;
    private Date a_MeasuredAt;
    private int a_SensorID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_VALUE = "Value";
    public static final String DBNAME_MEASUREDINCREMENT = "MeasuredIncrement";
    public static final String DBNAME_MEASUREDAT = "MeasuredAt";
    public static final String DBNAME_SENSOR_ID = "SensorID";

    // Constructors
    private T_Measurement() {}

    // Creations
    public static T_Measurement CreateFromRetrieved(int pk, Dictionary dict) {
        T_Measurement temp = new T_Measurement();

        temp.a_pk = pk;
        temp.a_Value = (int)dict.get(DBNAME_VALUE);
        temp.a_MeasuredIncrement = (int)dict.get(DBNAME_MEASUREDINCREMENT);
        temp.a_MeasuredAt = (Date)dict.get(DBNAME_MEASUREDAT);
        temp.a_SensorID = (int)dict.get(DBNAME_SENSOR_ID);

        return temp;
    }

    public static T_Measurement CreateFromScratch(Dictionary dict) {
        T_Measurement temp = new T_Measurement();

        temp.a_Value = (int)dict.get(DBNAME_VALUE);
        temp.a_MeasuredIncrement = (int)dict.get(DBNAME_MEASUREDINCREMENT);
        temp.a_MeasuredAt = (Date)dict.get(DBNAME_MEASUREDAT);
        temp.a_SensorID = (int)dict.get(DBNAME_SENSOR_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Measurement tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_VALUE, tmp.getA_Value());
        jo.put(DBNAME_MEASUREDINCREMENT, tmp.getA_MeasuredIncrement());
        jo.put(DBNAME_MEASUREDAT, tmp.getA_MeasuredAt());
        jo.put(DBNAME_SENSOR_ID, tmp.getA_SensorID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsIntOk(a_Value) &&
                Assurance.IsIntOk(a_MeasuredIncrement) &&
                Assurance.IsDateOk(a_MeasuredAt) &&
                Assurance.IsIntOk(a_SensorID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsIntOk(a_Value) &&
                Assurance.IsIntOk(a_MeasuredIncrement) &&
                Assurance.IsDateOk(a_MeasuredAt) &&
                Assurance.IsIntOk(a_SensorID);
    }

    @Override
    public String InfoPrintAllColumns(){
        return  "id:PK | " +
                "Value: varchar | " +
                "MeasuredIncrement: int | " +
                "MeasuredAt: Date | " +
                "SensorID: int";
    }

    // Generic
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("[");

        if (Assurance.IsIntOk(a_pk))
            str.append("ID: " + a_pk + "; ");

        if (Assurance.IsIntOk(a_Value))
            str.append("Value " + a_Value + "; ");

        if (Assurance.IsIntOk(a_MeasuredIncrement))
            str.append("MeasuredIncrement: " + a_MeasuredIncrement + "; ");

        if (Assurance.IsDateOk(a_MeasuredAt))
            str.append("MeasuredAt: " + a_MeasuredAt + "; ");

        if (Assurance.IsIntOk(a_SensorID))
            str.append("SensorID: " + a_SensorID + "; ");

        str.append("]");

        return str.toString();
    }


    // Getters

    public int getA_pk() {
        return a_pk;
    }

    public int getA_Value() {
        return a_Value;
    }

    public int getA_MeasuredIncrement() {
        return a_MeasuredIncrement;
    }

    public Date getA_MeasuredAt() {
        return a_MeasuredAt;
    }

    public int getA_SensorID() {
        return a_SensorID;
    }
}
