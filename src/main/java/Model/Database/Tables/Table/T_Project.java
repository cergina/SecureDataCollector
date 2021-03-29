/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;

public class T_Project extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "project" : "project";

    // Atributes
    private int a_pk;
    private String a_Name;
    private Date a_CreatedAt;
    private Date a_DeletedAt;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_NAME = "Name";
    public static final String DBNAME_CreatedAt = "CreatedAt";
    public static final String DBNAME_DeletedAt = "DeletedAt";

    public static T_Project REFERENCE = new T_Project();
    public static String[] TABLE_CODENAMES = {
            "Name", "Created At", "Deleted at"
    };

    // Constructors
    private T_Project() {}

    // Creations

    public static T_Project CreateFromRetrieved(int pk, Dictionary tmpDict, Date deleted) {
        T_Project temp = new T_Project();

        temp.a_pk = pk;
        temp.a_Name = (String)tmpDict.get(DBNAME_NAME);
        temp.a_CreatedAt = (Date)tmpDict.get(DBNAME_CreatedAt);
        temp.a_DeletedAt = deleted;

        return temp;
    }

    public static T_Project CreateFromScratch(String name) {
        T_Project temp = new T_Project();

        temp.a_Name = name;
        temp.a_CreatedAt = Date.valueOf(LocalDate.now());

        return temp;
    }

    // As JSON
    public static JSONObject MakeJSONObjectFrom(T_Project tmp) {
        JSONObject jo = new JSONObject();

        jo.put(DBNAME_ID, tmp.getA_pk());
        jo.put(DBNAME_NAME, tmp.getA_Name());
        jo.put(DBNAME_CreatedAt, tmp.getA_CreatedAt());
        if (tmp.getA_DeletedAt() != null)
            jo.put(DBNAME_DeletedAt, tmp.getA_DeletedAt());

        return jo;
    }


    // Interface specific

    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsDateOk(a_CreatedAt) &&
                Assurance.IsVarcharOk(a_Name);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsDateOk(a_CreatedAt) &&
                Assurance.IsVarcharOk(a_Name);
    }


    @Override
    public String InfoPrintAllColumns() {
        return  "id:PK | " +
                "name: varchar | " +
                "created: Date | " +
                "deleted: Date";
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

        str.add(this.a_Name);
        str.add(this.a_CreatedAt.toString());
        str.add(this.a_DeletedAt.toString());

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

        if (Assurance.IsIntOk(a_pk))
            str.append("ID: " + a_pk + "; ");

        if (Assurance.IsVarcharOk(a_Name))
            str.append("name " + a_Name + "; ");

        if (Assurance.IsDateOk(a_CreatedAt))
            str.append("created: " + a_CreatedAt + "; ");

        if (Assurance.IsDateOk(a_DeletedAt))
            str.append("deleted: " + a_DeletedAt + "; ");

        str.append("]");

        return str.toString();
    }

    // Getters
    public int getA_pk() {
        return a_pk;
    }

    public String getA_Name() {
        return a_Name;
    }

    public Date getA_CreatedAt() {
        return a_CreatedAt;
    }

    public Date getA_DeletedAt() {
        return a_DeletedAt;
    }
}
