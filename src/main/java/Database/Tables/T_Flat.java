package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class T_Flat  implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "flat" : "flat";

    // Atributes
    private int a_pk;
    private String a_ApartmentNO;
    private int a_AddressID;

    public static final String DBNAME_APARTMENTNO = "ApartmentNO";
    public static final String DBNAME_ADDRESS_ID = "AddressID";

    // Constructors
    private T_Flat() {}

    // Creations


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_ApartmentNO) &&
                Assurance.IsIntOk(a_AddressID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_ApartmentNO) &&
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

    public String getA_ApartmentNO() {
        return a_ApartmentNO;
    }

    public int getA_AddressID() {
        return a_AddressID;
    }
}
