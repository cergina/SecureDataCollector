package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;

public class T_CentralUnit implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "centralUnit" : "centralunit";

    // Atributes
    private int a_pk;
    private String a_Uid;
    private String a_FriendlyName;
    private String a_SimNO;
    private String a_Imei;
    private String a_Zwave;
    private int a_ProjectID;
    private int a_AddressID;

    public static final String DBNAME_UID = "Uid";
    public static final String DBNAME_FRIENDLYNAME = "FriendlyName";
    public static final String DBNAME_SIMNO = "SimNO";
    public static final String DBNAME_IMEI = "Imei";
    public static final String DBNAME_ZWAVE = "Zwave";
    public static final String DBNAME_PROJECTID = "ProjectID";
    public static final String DBNAME_ADDRESSID = "AddressID";

    // Constructors
    private T_CentralUnit() {}

    // Creations
    public static T_CentralUnit CreateFromRetrieved(int pk, Dictionary dict) {
        T_CentralUnit temp = new T_CentralUnit();

        temp.a_pk = pk;
        temp.a_Uid = (String)dict.get(DBNAME_UID);
        temp.a_FriendlyName = (String)dict.get(DBNAME_FRIENDLYNAME);
        temp.a_SimNO = (String)dict.get(DBNAME_SIMNO);
        temp.a_Imei = (String)dict.get(DBNAME_IMEI);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_ProjectID = (int)dict.get(DBNAME_PROJECTID);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESSID);

        return temp;
    }

    public static T_CentralUnit CreateFromScratch(Dictionary dict) {
        T_CentralUnit temp = new T_CentralUnit();

        temp.a_Uid = (String)dict.get(DBNAME_UID);
        temp.a_FriendlyName = (String)dict.get(DBNAME_FRIENDLYNAME);
        temp.a_SimNO = (String)dict.get(DBNAME_SIMNO);
        temp.a_Imei = (String)dict.get(DBNAME_IMEI);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_ProjectID = (int)dict.get(DBNAME_PROJECTID);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESSID);

        return temp;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Uid) &&
                Assurance.IsVarcharOk(a_FriendlyName) &&
                Assurance.IsVarcharOk(a_SimNO) &&
                Assurance.IsVarcharOk(a_Imei) &&
                Assurance.IsVarcharOk(a_Zwave) &&
                Assurance.IsIntOk(a_ProjectID) &&
                Assurance.IsIntOk(a_AddressID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Uid) &&
                Assurance.IsVarcharOk(a_FriendlyName) &&
                Assurance.IsVarcharOk(a_SimNO) &&
                Assurance.IsVarcharOk(a_Imei) &&
                Assurance.IsVarcharOk(a_Zwave) &&
                Assurance.IsIntOk(a_ProjectID) &&
                Assurance.IsIntOk(a_AddressID);
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

    public String getA_Uid() {
        return a_Uid;
    }

    public String getA_FriendlyName() {
        return a_FriendlyName;
    }

    public String getA_SimNO() {
        return a_SimNO;
    }

    public String getA_Imei() {
        return a_Imei;
    }

    public String getA_Zwave() {
        return a_Zwave;
    }

    public int getA_ProjectID() {
        return a_ProjectID;
    }

    public int getA_AddressID() {
        return a_AddressID;
    }
}
