package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.List;

public class T_LoginLog extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "loginLog" : "loginlog";

    // Atributes
    private int a_pk;
    private Date a_LoggedAt;
    private String a_SrcIp;
    private int a_UserId;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_LOGGEDAT = "LoggedAt";
    public static final String DBNAME_SRCIP = "SrcIp";
    public static final String DBNAME_USERID = "UserID";

    public static T_LoginLog REFERENCE = new T_LoginLog();
    public static String[] TABLE_CODENAMES = {
            "Logged  At", "Source Ip", "User Id"
    };

    // Constructors
    private T_LoginLog() {}

    // Creations
    public static T_LoginLog CreateFromRetrieved(int pk, Dictionary dict) {
        T_LoginLog temp = new T_LoginLog();

        temp.a_pk = pk;
        temp.a_LoggedAt = (Date)dict.get(DBNAME_LOGGEDAT);
        temp.a_SrcIp = (String) dict.get(DBNAME_SRCIP);
        temp.a_UserId = (int)dict.get(DBNAME_USERID);

        return temp;
    }

    public static T_LoginLog CreateFromScratch(Dictionary dict) {
        T_LoginLog temp = new T_LoginLog();

        temp.a_LoggedAt = Date.valueOf(LocalDate.now());
        temp.a_SrcIp = (String) dict.get(DBNAME_SRCIP);
        temp.a_UserId = (int)dict.get(DBNAME_USERID);

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_LoginLog tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_LOGGEDAT, tmp.getA_LoggedAt());
        jo.put(DBNAME_SRCIP, tmp.getA_SrcIp());
        jo.put(DBNAME_USERID, tmp.getA_UserId());

        return jo;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isDateOk(a_LoggedAt) &&
                Assurance.isVarcharOk(a_SrcIp) &&
                Assurance.isFkOk(a_UserId);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isDateOk(a_LoggedAt) &&
                Assurance.isVarcharOk(a_SrcIp) &&
                Assurance.isFkOk(a_UserId);
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

        str.add(this.a_LoggedAt.toString());
        str.add(this.a_SrcIp);
        str.add(Integer.toString(a_UserId));

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

    public Date getA_LoggedAt() {
        return a_LoggedAt;
    }

    public String getA_SrcIp() {
        return a_SrcIp;
    }

    public int getA_UserId() {
        return a_UserId;
    }
}
