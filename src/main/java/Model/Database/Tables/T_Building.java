package Model.Database.Tables;

import Model.Database.Interaction.I_Building;
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

public class T_Building extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "building" : "building";

    // Atributes
    private int a_pk;
    private int a_ProjectID;
    private int a_AddressID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_PROJECT_ID = "ProjectID";
    public static final String DBNAME_ADDRESS_ID = "AddressID";

    public static T_Building REFERENCE = new T_Building();
    public static String[] TABLE_CODENAMES = {
            "Project ID", "Address ID"
    };

    // Constructors
    protected T_Building() {}

    // Creations
    public static T_Building CreateFromRetrieved(int pk, Dictionary dict) {
        T_Building temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_Building CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_Building CreateBase(Dictionary dict) {
        T_Building temp = new T_Building();

        temp.a_ProjectID = (int)dict.get(DBNAME_PROJECT_ID);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESS_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Building tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_PROJECT_ID, tmp.getA_ProjectID());
        jo.put(DBNAME_ADDRESS_ID, tmp.getA_AddressID());

        return jo;
    }

    // From DbEntity
    public T_Building FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_Building.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isFkOk(a_ProjectID) &&
                Assurance.isFkOk(a_AddressID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isFkOk(a_ProjectID) &&
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

        str.add(Integer.toString(a_ProjectID));
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

    public int getA_ProjectID() {
        return a_ProjectID;
    }

    public int getA_AddressID() {
        return a_AddressID;
    }
}
