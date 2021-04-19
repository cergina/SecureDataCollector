/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Model.Database.Tables;

import Model.Database.Interaction.I_Project;
import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.List;

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
    protected T_Project() {}

    // Creations

    public static T_Project CreateFromRetrieved(int pk, Dictionary dict) {
        T_Project temp = new T_Project();

        temp.a_pk = pk;
        temp.a_Name = (String)dict.get(DBNAME_NAME);
        temp.a_CreatedAt = (Date)dict.get(DBNAME_CreatedAt);
        temp.a_DeletedAt = (Date)dict.get(DBNAME_DeletedAt);

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

    // From DbEntity
    public T_Project FillEntityFromResultSet(ResultSet rs) throws SQLException {
        return I_Project.FillEntity(rs);
    }

    @Override
    public String GetDbTableName() {
        return DBTABLE_NAME;
    }


    // Interface specific

    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.isDateOk(a_CreatedAt) &&
                Assurance.isVarcharOk(a_Name);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.isFkOk(a_pk) &&
                Assurance.isDateOk(a_CreatedAt) &&
                Assurance.isVarcharOk(a_Name);
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
    public List<String> GenerateHtmlTableRow_FromDbRow() {
        List<String> str = super.GenerateHtmlTableRow_FromDbRow();

        str.add(this.a_Name);
        str.add(this.a_CreatedAt.toString());
        str.add((this.a_DeletedAt == null) ? "" : this.a_DeletedAt.toString());

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

        if (Assurance.isFkOk(a_pk))
            str.append("ID: " + a_pk + "; ");

        if (Assurance.isVarcharOk(a_Name))
            str.append("name " + a_Name + "; ");

        if (Assurance.isDateOk(a_CreatedAt))
            str.append("created: " + a_CreatedAt + "; ");

        if (Assurance.isDateOk(a_DeletedAt))
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
