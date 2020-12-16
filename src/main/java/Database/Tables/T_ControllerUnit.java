package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;

public class T_ControllerUnit  implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "controllerUnit" : "controllerunit";

    // Atributes
    private int a_pk;
    private int a_Uid;
    private String a_DipAddress;
    private String a_Zwave;
    private int a_CentralUnitID;
    private int a_FlatID;

    public static final String DBNAME_UID = "Uid";
    public static final String DBNAME_DIPADDRESS = "DipAddress";
    public static final String DBNAME_ZWAVE = "Zwave";
    public static final String DBNAME_CENTRALUNIT_ID = "CentralUnitID";
    public static final String DBNAME_FLAT_ID = "FlatID";

    // Constructors
    private T_ControllerUnit() {}

    // Creations
    public static T_ControllerUnit CreateFromRetrieved(int pk, Dictionary dict) {
        T_ControllerUnit temp = new T_ControllerUnit();

        temp.a_pk = pk;
        temp.a_Uid = (int)dict.get(DBNAME_UID);
        temp.a_DipAddress = (String)dict.get(DBNAME_DIPADDRESS);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_CentralUnitID = (int)dict.get(DBNAME_CENTRALUNIT_ID);
        temp.a_FlatID = (int)dict.get(DBNAME_FLAT_ID);

        return temp;
    }

    public static T_ControllerUnit CreateFromScratch(Dictionary dict) {
        T_ControllerUnit temp = new T_ControllerUnit();

        temp.a_Uid = (int)dict.get(DBNAME_UID);
        temp.a_DipAddress = (String)dict.get(DBNAME_DIPADDRESS);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_CentralUnitID = (int)dict.get(DBNAME_CENTRALUNIT_ID);
        temp.a_FlatID = (int)dict.get(DBNAME_FLAT_ID);

        return temp;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsIntOk(a_Uid) &&
                Assurance.IsVarcharOk(a_DipAddress) &&
                Assurance.IsVarcharOk(a_Zwave) &&
                Assurance.IsIntOk(a_CentralUnitID) &&
                Assurance.IsIntOk(a_FlatID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsIntOk(a_Uid) &&
                Assurance.IsVarcharOk(a_DipAddress) &&
                Assurance.IsVarcharOk(a_Zwave) &&
                Assurance.IsIntOk(a_CentralUnitID) &&
                Assurance.IsIntOk(a_FlatID);
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

    public int getA_Uid() {
        return a_Uid;
    }

    public String getA_DipAddress() {
        return a_DipAddress;
    }

    public String getA_Zwave() {
        return a_Zwave;
    }

    public int getA_CentralUnitID() {
        return a_CentralUnitID;
    }

    public int getA_FlatID() {
        return a_FlatID;
    }
}
