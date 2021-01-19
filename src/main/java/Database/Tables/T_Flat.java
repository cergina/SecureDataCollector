package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;
import Database.Support.DBToHtml;
import Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Dictionary;

public class T_Flat  extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "flat" : "flat";

    // Atributes
    private int a_pk;
    private String a_ApartmentNO;
    private int a_AddressID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_APARTMENTNO = "ApartmentNO";
    public static final String DBNAME_ADDRESS_ID = "AddressID";

    public static T_Flat REFERENCE = new T_Flat();
    public static String[] TABLE_CODENAMES = {
            "Aparment Number", "Address ID"
    };

    // Constructors
    private T_Flat() {}

    // Creations
    public static T_Flat CreateFromRetrieved(int pk, Dictionary dict) {
        T_Flat temp = new T_Flat();

        temp.a_pk = pk;
        temp.a_ApartmentNO = (String)dict.get(DBNAME_APARTMENTNO);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESS_ID);

        return temp;
    }

    public static T_Flat CreateFromScratch(Dictionary dict) {
        T_Flat temp = new T_Flat();

        temp.a_ApartmentNO = (String)dict.get(DBNAME_APARTMENTNO);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESS_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Flat tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_APARTMENTNO, tmp.getA_ApartmentNO());
        jo.put(DBNAME_ADDRESS_ID, tmp.getA_AddressID());

        return jo;
    }

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

    @Override
    public String ReturnDBNamesInHtmlRow() {
        String documentPart = "";
        documentPart += "<tr>";

        for (String str:TABLE_CODENAMES
             ) {
            documentPart += "<th>" + str + "</th>";
        }

        documentPart += "<th></th>";
        documentPart += "</tr>";

        return documentPart;
    }

    // For HTML purposes
    @Override
    public ArrayList<String> GenerateHtmlTableRow_FromDbRow() {
        ArrayList<String> str = super.GenerateHtmlTableRow_FromDbRow();

        str.add(this.a_ApartmentNO);
        str.add(Integer.toString(a_AddressID));

        return str;
    }

    @Override
    public String[] GetTableCodeNames() {
        return TABLE_CODENAMES;
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
