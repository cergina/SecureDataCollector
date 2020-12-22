package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DbConfig;
import org.json.JSONObject;

import java.util.Dictionary;

public class T_Address implements DBTable {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "address" : "address";

    // Atributes
    private int a_pk;
    private String a_Country;
    private String a_City;
    private String a_Street;
    private String a_HouseNO;
    private String a_ZIP;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_COUNTRY = "Country";
    public static final String DBNAME_CITY = "City";
    public static final String DBNAME_STREET = "Street";
    public static final String DBNAME_HOUSENO = "HouseNO";
    public static final String DBNAME_ZIP = "Zip";

    // Constructors
    private T_Address() {}

    // Creations
    public static T_Address CreateFromRetrieved(int pk, Dictionary dict) {
        T_Address temp = new T_Address();

        temp.a_pk = pk;
        temp.a_Country = (String)dict.get(DBNAME_COUNTRY);
        temp.a_City = (String)dict.get(DBNAME_CITY);
        temp.a_Street = (String)dict.get(DBNAME_STREET);
        temp.a_HouseNO = (String)dict.get(DBNAME_HOUSENO);
        temp.a_ZIP = (String)dict.get(DBNAME_ZIP);

        return temp;
    }

    public static T_Address CreateFromScratch(Dictionary dict) {
        T_Address temp = new T_Address();

        temp.a_Country = (String)dict.get(DBNAME_COUNTRY);
        temp.a_City = (String)dict.get(DBNAME_CITY);
        temp.a_Street = (String)dict.get(DBNAME_STREET);
        temp.a_HouseNO = (String)dict.get(DBNAME_HOUSENO);
        temp.a_ZIP = (String)dict.get(DBNAME_ZIP);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Address tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_COUNTRY, tmp.getA_Country());
        jo.put(DBNAME_CITY, tmp.getA_City());
        jo.put(DBNAME_STREET, tmp.getA_Street());
        jo.put(DBNAME_HOUSENO, tmp.getA_HouseNO());
        jo.put(DBNAME_ZIP, tmp.getA_ZIP());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_City) &&
                Assurance.IsVarcharOk(a_Country) &&
                Assurance.IsVarcharOk(a_HouseNO) &&
                Assurance.IsVarcharOk(a_Street) &&
                Assurance.IsVarcharOk(a_ZIP);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_City) &&
                Assurance.IsVarcharOk(a_Country) &&
                Assurance.IsVarcharOk(a_HouseNO) &&
                Assurance.IsVarcharOk(a_Street) &&
                Assurance.IsVarcharOk(a_ZIP);
    }


    @Override
    public String InfoPrintAllColumns() {
        return  "ID:PK | " +
                "Country: varchar | " +
                "City: varchar | " +
                "Street: varchar | " +
                "HouseNO: varchar | " +
                "ZIP: varchar";
    }

    // Generic
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");

        if (Assurance.IsIntOk(a_pk))
            str.append("ID: " + a_pk + "; ");

        if (Assurance.IsVarcharOk(a_Country))
            str.append("Country " + a_Country + "; ");

        if (Assurance.IsVarcharOk(a_Street))
            str.append("Street " + a_Street + "; ");

        if (Assurance.IsVarcharOk(a_HouseNO))
            str.append("HouseNO " + a_HouseNO + "; ");

        if (Assurance.IsVarcharOk(a_City))
            str.append("City " + a_City + "; ");

        if (Assurance.IsVarcharOk(a_ZIP))
            str.append("ZIP " + a_ZIP + "; ");

        str.append("]");

        return str.toString();
    }

    // Getters
    public int getA_pk() {
        return a_pk;
    }
    public String getA_Country() { return a_Country; }
    public String getA_City() { return  a_City; }
    public String getA_Street() { return  a_Street; }
    public String getA_HouseNO() { return  a_HouseNO; }
    public String getA_ZIP() { return a_ZIP; }
}
