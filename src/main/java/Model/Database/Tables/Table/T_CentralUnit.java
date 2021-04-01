package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;
import java.util.List;

public class T_CentralUnit extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "centralUnit" : "centralunit";

    // Atributes
    private int a_pk;
    private int a_Uid;
    private String a_FriendlyName;
    private String a_SimNO;
    private String a_Imei;
    private String a_Zwave;
    private int a_ProjectID;
    private int a_AddressID;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_UID = "Uid";
    public static final String DBNAME_FRIENDLYNAME = "FriendlyName";
    public static final String DBNAME_SIMNO = "SimNO";
    public static final String DBNAME_IMEI = "Imei";
    public static final String DBNAME_ZWAVE = "Zwave";
    public static final String DBNAME_PROJECT_ID = "ProjectID";
    public static final String DBNAME_ADDRESS_ID = "AddressID";

    public static T_CentralUnit REFERENCE = new T_CentralUnit();
    public static String[] TABLE_CODENAMES = {
            "UID", "Friendly Name", "SIM Number", "IMEI code", "Zwave number", "Project ID", "Address ID"
    };

    // Constructors
    private T_CentralUnit() {}

    // Creations
    public static T_CentralUnit CreateFromRetrieved(int pk, Dictionary dict) {
        T_CentralUnit temp = new T_CentralUnit();

        temp.a_pk = pk;
        temp.a_Uid = (int)dict.get(DBNAME_UID);
        temp.a_FriendlyName = (String)dict.get(DBNAME_FRIENDLYNAME);
        temp.a_SimNO = (String)dict.get(DBNAME_SIMNO);
        temp.a_Imei = (String)dict.get(DBNAME_IMEI);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_ProjectID = (int)dict.get(DBNAME_PROJECT_ID);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESS_ID);

        return temp;
    }

    public static T_CentralUnit CreateFromScratch(Dictionary dict) {
        T_CentralUnit temp = new T_CentralUnit();

        temp.a_Uid = (int)dict.get(DBNAME_UID);
        temp.a_FriendlyName = (String)dict.get(DBNAME_FRIENDLYNAME);
        temp.a_SimNO = (String)dict.get(DBNAME_SIMNO);
        temp.a_Imei = (String)dict.get(DBNAME_IMEI);
        temp.a_Zwave = (String)dict.get(DBNAME_ZWAVE);
        temp.a_ProjectID = (int)dict.get(DBNAME_PROJECT_ID);
        temp.a_AddressID = (int)dict.get(DBNAME_ADDRESS_ID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_CentralUnit tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_UID, tmp.getA_Uid());
        jo.put(DBNAME_FRIENDLYNAME, tmp.getA_FriendlyName());
        jo.put(DBNAME_SIMNO, tmp.getA_SimNO());
        jo.put(DBNAME_IMEI, tmp.getA_Imei());
        jo.put(DBNAME_ZWAVE, tmp.getA_Zwave());
        jo.put(DBNAME_PROJECT_ID, tmp.getA_ProjectID());
        jo.put(DBNAME_ADDRESS_ID, tmp.getA_AddressID());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsIntOk(a_Uid) &&
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
                Assurance.IsIntOk(a_Uid) &&
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
        str.add(this.a_FriendlyName);
        str.add(this.a_SimNO);
        str.add(this.a_Imei);
        str.add(this.a_Zwave);
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

    public int getA_Uid() {
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
