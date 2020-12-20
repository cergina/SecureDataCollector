/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Database.Tables;

import Database.Support.Assurance;
import Database.Support.DBTable;

import java.sql.Date;
import java.time.LocalDate;

public class T_Project implements DBTable {
    // Atributes
    private int a_pk;
    private String a_name;
    private Date a_created;
    private Date a_deleted;

    // Constructors
    private T_Project() {}

    // Creations

    public static T_Project CreateFromRetrieved(int pk, String name, Date created, Date deleted) {
        T_Project temp = new T_Project();

        temp.a_pk = pk;
        temp.a_name = name;
        temp.a_created = created;
        temp.a_deleted = deleted;

        return temp;
    }

    public static T_Project CreateFromScratch(String name) {
        T_Project temp = new T_Project();

        temp.a_name = name;
        temp.a_created = Date.valueOf(LocalDate.now());

        return temp;
    }

    //


    // Interface specific

    @Override
    public boolean IsTableOkForDatabaseEnter() {
        return Assurance.IsDateOk(a_created) &&
                Assurance.IsVarcharOk(a_name);
    }

    @Override
    public boolean WasTableWithdrawedCorrectlyFromDatabase() {
        return Assurance.IsIntOk(a_pk) &&
                Assurance.IsDateOk(a_created) &&
                Assurance.IsVarcharOk(a_name);
    }


    @Override
    public String InfoPrintAllColumns() {
        return  "id:PK | " +
                "name: varchar | " +
                "created: Date | " +
                "deleted: Date";
    }

    // Generic
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");

        if (Assurance.IsIntOk(a_pk))
            str.append("ID: " + a_pk + "; ");

        if (Assurance.IsVarcharOk(a_name))
            str.append("name " + a_name + "; ");

        if (Assurance.IsDateOk(a_created))
            str.append("created: " + a_created + "; ");

        if (Assurance.IsDateOk(a_deleted))
            str.append("deleted: " + a_deleted + "; ");

        str.append("]");

        return str.toString();
    }

    // Getters
    public int getA_pk() {
        return a_pk;
    }

    public String getA_name() {
        return a_name;
    }

    public Date getA_created() {
        return a_created;
    }

    public Date getA_deleted() {
        return a_deleted;
    }
}
