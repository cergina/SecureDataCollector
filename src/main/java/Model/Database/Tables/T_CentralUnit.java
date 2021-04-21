package Model.Database.Tables;

import Model.Database.Interaction.I_CentralUnit;
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

public class T_CentralUnit extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "centralUnit" : "centralunit";

    // Atributes
    private int a_pk;
    private int a_Uid;
    private String a_DipAddress;
    private String a_FriendlyName;
    private String a_SimNO;
    private String a_Imei;
    private String a_Zwave;
    private int a_BuildingID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_UID = "Uid";
    public static final String DBNAME_DIPADDRESS = "DipAddress";
    public static final String DBNAME_FRIENDLYNAME = "FriendlyName";
    public static final String DBNAME_SIMNO = "SimNO";
    public static final String DBNAME_IMEI = "Imei";
    public static final String DBNAME_ZWAVE = "Zwave";
    public static final String DBNAME_BUILDING_ID = "BuildingID";

    public static T_CentralUnit REFERENCE = new T_CentralUnit();
    public static String[] TABLE_CODENAMES = {
            "UID", "Dip Address", "Friendly Name", "SIM Number", "IMEI code", "Zwave number", "Building ID"
    };

    // Constructors
    protected T_CentralUnit() {}

    // Creations
    public static T_CentralUnit CreateFromRetrieved(int pk, Dictionary dict) {
        T_CentralUnit temp = CreateBase(dict);

        temp.a_pk = pk;

        return temp;
    }

    public static T_CentralUnit CreateFromScratch(Dictionary dict) {
        return CreateBase(dict);
    }

    private static T_CentralUnit CreateBase(Dictionary dict) {
        T_CentralUnit temp = new T_CentralUnit();

        temp.a_Uid = (int)dict.get(DBNAME_UID);
        temp.a_DipAddress = (String)dict.get(DBNAME_DIPADDRESS);
        temp.a_FriendlyName = (String)dict.get(DBNAME_FRIENDLYNAME);
        temp.a_SimNO = (String)dict.get(DBNAME_SIMNO);
        temp.a_Imei = (String)dict.get(DBNAME_IMEI);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_BuildingID = (int)dict.get(DBNAME_BUILDING_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_CentralUnit tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_UID, tmp.getA_Uid());
        jo.put(DBNAME_DIPADDRESS, tmp.getA_DipAddress());
        jo.put(DBNAME_FRIENDLYNAME, tmp.getA_FriendlyName());
        jo.put(DBNAME_SIMNO, tmp.getA_SimNO());
        jo.put(DBNAME_IMEI, tmp.getA_Imei());
        jo.put(DBNAME_ZWAVE, tmp.getA_Zwave());
        jo.put(DBNAME_BUILDING_ID, tmp.getA_BuildingID());

        return jo;
    }

    // From DbEntity
    public T_CentralUnit FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_CentralUnit.FillEntity(rs);
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
                Assurance.isVarcharOk(a_FriendlyName) &&
                Assurance.isVarcharOk(a_SimNO) &&
                Assurance.isVarcharOk(a_Imei) &&
                Assurance.isVarcharOk(a_Zwave) &&
                Assurance.isFkOk(a_BuildingID);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isIntOk(a_Uid) &&
                Assurance.isVarcharOk(a_DipAddress) &&
                Assurance.isVarcharOk(a_FriendlyName) &&
                Assurance.isVarcharOk(a_SimNO) &&
                Assurance.isVarcharOk(a_Imei) &&
                Assurance.isVarcharOk(a_Zwave) &&
                Assurance.isFkOk(a_BuildingID);
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
        str.add(this.a_FriendlyName);
        str.add(this.a_SimNO);
        str.add(this.a_Imei);
        str.add(this.a_Zwave);
        str.add(Integer.toString(a_BuildingID));

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

    public int getA_BuildingID() {
        return a_BuildingID;
    }
}
