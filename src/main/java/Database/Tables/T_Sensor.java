package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class T_Sensor implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "sensor" : "sensor";

    // Atributes
    private int a_pk;
    private String a_Input;
    private String a_Name;
    private int a_SensorTypeID;
    private int a_ControllerUnitID;

    public static final String DBNAME_INPUT = "Input";
    public static final String DBNAME_NAME = "Name";
    public static final String DBNAME_SENSORTYPE_ID = "SensorTypeID";
    public static final String DBNAME_CONTROLLERUNIT_ID = "ControllerUnitID";


    // Constructors
    private T_Sensor() {}

    // Creations


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
