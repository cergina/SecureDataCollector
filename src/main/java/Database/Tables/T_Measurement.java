package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Date;
import java.util.Dictionary;

public class T_Measurement implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "measurement" : "measurement";

    // Atributes
    private int a_pk;
    private String a_Value;
    private int a_MeasuredIncrement;
    private Date a_MeasuredAt;
    private int a_SensorID;

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
        temp.a_Value = (String)dict.get(DBNAME_VALUE);
        temp.a_MeasuredIncrement = (int)dict.get(DBNAME_MEASUREDINCREMENT);
        temp.a_MeasuredAt = (Date)dict.get(DBNAME_MEASUREDAT);
        temp.a_SensorID = (int)dict.get(DBNAME_SENSOR_ID);

        return temp;
    }

    public static T_Measurement CreateFromScratch(Dictionary dict) {
        T_Measurement temp = new T_Measurement();

        temp.a_Value = Integer.toString((int)dict.get(DBNAME_VALUE));
        temp.a_MeasuredIncrement = (int)dict.get(DBNAME_MEASUREDINCREMENT);
        temp.a_MeasuredAt = (Date)dict.get(DBNAME_MEASUREDAT);
        temp.a_SensorID = (int)dict.get(DBNAME_SENSOR_ID);

        return temp;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Value) &&
                Assurance.IsIntOk(a_MeasuredIncrement) &&
                Assurance.IsDateOk(a_MeasuredAt) &&
                Assurance.IsIntOk(a_SensorID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Value) &&
                Assurance.IsIntOk(a_MeasuredIncrement) &&
                Assurance.IsDateOk(a_MeasuredAt) &&
                Assurance.IsIntOk(a_SensorID);
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

    public String getA_Value() {
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
