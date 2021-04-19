package Model.Database.Tables;

import Model.Database.Interaction.I_ControllerUnit;
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

public class T_ControllerUnit extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "controllerUnit" : "controllerunit";

    // Atributes
    private int a_pk;
    private int a_Uid;
    private String a_DipAddress;
    private String a_Zwave;
    private int a_CentralUnitID;
    private int a_FlatID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_UID = "Uid";
    public static final String DBNAME_DIPADDRESS = "DipAddress";
    public static final String DBNAME_ZWAVE = "Zwave";
    public static final String DBNAME_CENTRALUNIT_ID = "CentralUnitID";
    public static final String DBNAME_FLAT_ID = "FlatID";

    public static T_ControllerUnit REFERENCE = new T_ControllerUnit();
    public static String[] TABLE_CODENAMES = {
            "UID", "DIP Address", "Zwave code", "Central Unit ID", "Flat ID"
    };

    // Constructors
    protected T_ControllerUnit() {}

    // Creations
    public static T_ControllerUnit CreateFromRetrieved(int pk, Dictionary dict) {
        T_ControllerUnit temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_ControllerUnit CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_ControllerUnit CreateBase(Dictionary dict) {
        T_ControllerUnit temp = new T_ControllerUnit();

        temp.a_Uid = (int)dict.get(DBNAME_UID);
        temp.a_DipAddress = (String)dict.get(DBNAME_DIPADDRESS);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_CentralUnitID = (int)dict.get(DBNAME_CENTRALUNIT_ID);
        temp.a_FlatID = (int)dict.get(DBNAME_FLAT_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_ControllerUnit tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_UID, tmp.getA_Uid());
        jo.put(DBNAME_DIPADDRESS, tmp.getA_DipAddress());
        jo.put(DBNAME_ZWAVE, tmp.getA_Zwave());
        jo.put(DBNAME_CENTRALUNIT_ID, tmp.getA_CentralUnitID());
        jo.put(DBNAME_FLAT_ID, tmp.getA_FlatID());

        return jo;
    }


    // From DbEntity
    public T_ControllerUnit FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_ControllerUnit.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isIntOk(a_Uid) &&
                Assurance.isVarcharOk(a_DipAddress) &&
                Assurance.isVarcharOk(a_Zwave) &&
                Assurance.isFkOk(a_CentralUnitID) &&
                Assurance.isFkOk(a_FlatID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isIntOk(a_Uid) &&
                Assurance.isVarcharOk(a_DipAddress) &&
                Assurance.isVarcharOk(a_Zwave) &&
                Assurance.isFkOk(a_CentralUnitID) &&
                Assurance.isFkOk(a_FlatID);
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

        str.add(Integer.toString(a_Uid));
        str.add(this.a_DipAddress);
        str.add(this.a_Zwave);
        str.add(Integer.toString(a_CentralUnitID));
        str.add(Integer.toString(a_FlatID));

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
