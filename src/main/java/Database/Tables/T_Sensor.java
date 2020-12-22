package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;

public class T_Sensor implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "sensor" : "sensor";

    // Atributes
    private int a_pk;
    private String a_Input;
    private String a_Name;
    private int a_SensorTypeID;
    private int a_ControllerUnitID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_INPUT = "Input";
    public static final String DBNAME_NAME = "Name";
    public static final String DBNAME_SENSORTYPE_ID = "SensorTypeID";
    public static final String DBNAME_CONTROLLERUNIT_ID = "ControllerUnitID";


    // Constructors
    private T_Sensor() {}

    // Creations
    public static T_Sensor CreateFromRetrieved(int pk, Dictionary dict) {
        T_Sensor temp = new T_Sensor();

        temp.a_pk = pk;
        temp.a_Input = (String)dict.get(DBNAME_INPUT);
        temp.a_Name = (String)dict.get(DBNAME_NAME);
        temp.a_SensorTypeID = (int)dict.get(DBNAME_SENSORTYPE_ID);
        temp.a_ControllerUnitID = (int)dict.get(DBNAME_CONTROLLERUNIT_ID);

        return temp;
    }

    public static T_Sensor CreateFromScratch(Dictionary dict) {
        T_Sensor temp = new T_Sensor();

        temp.a_Input = (String)dict.get(DBNAME_INPUT);
        temp.a_Name = (String)dict.get(DBNAME_NAME);
        temp.a_SensorTypeID = (int)dict.get(DBNAME_SENSORTYPE_ID);
        temp.a_ControllerUnitID = (int)dict.get(DBNAME_CONTROLLERUNIT_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Sensor tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_INPUT, tmp.getA_Input());
        jo.put(DBNAME_NAME, tmp.getA_Name());
        jo.put(DBNAME_SENSORTYPE_ID, tmp.getA_SensorTypeID());
        jo.put(DBNAME_CONTROLLERUNIT_ID, tmp.getA_ControllerUnitID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Input) &&
                Assurance.IsVarcharOk(a_Name) &&
                Assurance.IsIntOk(a_SensorTypeID) &&
                Assurance.IsIntOk(a_ControllerUnitID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Input) &&
                Assurance.IsVarcharOk(a_Name) &&
                Assurance.IsIntOk(a_SensorTypeID) &&
                Assurance.IsIntOk(a_ControllerUnitID);
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

    public String getA_Input() {
        return a_Input;
    }

    public String getA_Name() {
        return a_Name;
    }

    public int getA_SensorTypeID() {
        return a_SensorTypeID;
    }

    public int getA_ControllerUnitID() {
        return a_ControllerUnitID;
    }
}
