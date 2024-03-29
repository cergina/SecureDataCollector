package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.E_CommType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_CommType extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, E_CommType ec) throws SQLException {
        if (ec.IsEnumTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute E_CommType is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    E_CommType.DBNAME_NAME
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        E_CommType.DBTABLE_NAME +
                        "(" + tableNames + ") " +
                        "VALUES (?)"
        );

        int col = 0;
        ps.setString(++col, ec.getA_Name());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of CommType into db failed.");

        return affectedRows;
    }


    public static E_CommType retrieveByName(Connection conn, PreparedStatement ps, ResultSet rs, String name) throws SQLException {
        Assurance.varcharCheck(name);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_CommType.DBTABLE_NAME + " " +
                        "WHERE " + E_CommType.DBNAME_NAME + "=?"
        );

        int col = 0;
        ps.setString(++col, name);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        E_CommType ct = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ct = I_CommType.FillEntity(rs);
        }

        return ct;
    }

    // Privates
    public static E_CommType FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(E_CommType.DBNAME_NAME, rs.getString(E_CommType.DBNAME_NAME));

        return E_CommType.CreateFromRetrieved(rs.getInt(E_CommType.DBNAME_ID), dict);
    }
}
