package Model.Database.Tables;

import Model.Database.Interaction.I_AccessPrivillege;
import Model.Database.Interaction.I_Flat;
import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.List;

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
    protected T_Flat() {}

    // Creations
    public static T_Flat CreateFromRetrieved(int pk, Dictionary dict) {
        T_Flat temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_Flat CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_Flat CreateBase(Dictionary dict) {
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

    // From DbEntity
    public T_Flat FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_Flat.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isVarcharOk(a_ApartmentNO) &&
                Assurance.isFkOk(a_AddressID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isVarcharOk(a_ApartmentNO) &&
                Assurance.isFkOk(a_AddressID);
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
    public List<String> GenerateHtmlTableRow_FromDbRow() {
        List<String> str = super.GenerateHtmlTableRow_FromDbRow();

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
        StringBuilder str = new StringBuilder("[");

        return str.toString();
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
