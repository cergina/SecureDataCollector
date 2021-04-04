package Model.Database.Tables.Table;

import Model.Database.Support.Assurance;
import Model.Database.Support.DBTable;
import Model.Database.Support.DBToHtml;
import Model.Database.Support.DbConfig;
import Model.Database.Tables.DbEntity;

import java.util.Dictionary;

public class T_CommType extends DbEntity implements DBTable, DBToHtml {
    public static final String DBTABLE_NAME = DbConfig.DB_USE_CAMELCASE ? "commtype" : "commtype";

    // Atributes
    private int a_pk;
    private String a_Name;

    public static final String DBNAME_ID = "ID";
    public static final String DBNAME_NAME = "Name";

    public static T_CommType REFERENCE = new T_CommType();
    public static String[] TABLE_CODENAMES = {
            DBNAME_NAME
    };

    // Constructors
    private T_CommType() {}

    // Creations
    public static T_CommType CreateFromRetrieved(int pk, Dictionary dict) {
        T_CommType temp = new T_CommType();

        temp.a_pk = pk;
        temp.a_Name = (String)dict.get(DBNAME_NAME);

        return temp;
    }

    public static T_CommType CreateFromScratch(Dictionary dict) {
        T_CommType temp = new T_CommType();

        temp.a_Name = (String)dict.get(DBNAME_NAME);

        return temp;
    }

    // Interface specific
    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsVarcharOk(a_Name);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsVarcharOk(a_Name);
    }


    @Override
    public String InfoPrintAllColumns() {
        return  "ID:PK | " +
                "name: varchar";
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
            str.append("Name " + a_Name + "; ");

        str.append("]");

        return str.toString();
    }

    // Getters
    public int getA_pk() {
        return a_pk;
    }
    public String getA_Name() { return a_Name; }
}
