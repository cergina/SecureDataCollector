package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Date;

public class T_User implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "user" : "user";

    // Atributes
    private int a_pk;
    private String a_BeforeTitle;
    private String a_FirstName;
    private String a_MiddleName;
    private String a_LastName;
    private String a_Phone;
    private String a_Email;
    private String a_PermanentResidence;
    private boolean a_Blocked;

    public static final String DBNAME_BEFORETITLE = "BeforeTitle";
    public static final String DBNAME_FIRSTNAME = "FirstName";
    public static final String DBNAME_MIDDLENAME = "MiddleName";
    public static final String DBNAME_LASTNAME = "LastName";
    public static final String DBNAME_PHONE = "Phone";
    public static final String DBNAME_EMAIL = "Email";
    public static final String DBNAME_PERMANENTRESIDENCE = "PermanentResidence";
    public static final String DBNAME_BLOCKED = "Blocked";

    // Constructors
    private T_User() {}

    // Creations


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_BeforeTitle) &&
                Assurance.IsVarcharOk(a_FirstName) &&
                Assurance.IsVarcharOk(a_MiddleName) &&
                Assurance.IsVarcharOk(a_LastName) &&
                Assurance.IsVarcharOk(a_Phone) &&
                Assurance.IsVarcharOk(a_Email) &&
                Assurance.IsVarcharOk(a_PermanentResidence);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_BeforeTitle) &&
                Assurance.IsVarcharOk(a_FirstName) &&
                Assurance.IsVarcharOk(a_MiddleName) &&
                Assurance.IsVarcharOk(a_LastName) &&
                Assurance.IsVarcharOk(a_Phone) &&
                Assurance.IsVarcharOk(a_Email) &&
                Assurance.IsVarcharOk(a_PermanentResidence);
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

    public String getA_BeforeTitle() {
        return a_BeforeTitle;
    }

    public String getA_FirstName() {
        return a_FirstName;
    }

    public String getA_MiddleName() {
        return a_MiddleName;
    }

    public String getA_LastName() {
        return a_LastName;
    }

    public String getA_Phone() {
        return a_Phone;
    }

    public String getA_Email() {
        return a_Email;
    }

    public String getA_PermanentResidence() {
        return a_PermanentResidence;
    }

    public boolean isA_Blocked() {
        return a_Blocked;
    }
}
