package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Enum.E_AccessPrivilege;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_AccessPrivillege {
    public static int insert(Connection conn, PreparedStatement ps, E_AccessPrivilege ec) throws SQLException {
        if (ec.IsEnumTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute E_AccessPrivilege is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        E_AccessPrivilege.DBTABLE_NAME + "(" +
                        "Name" +
                        ") " +
                        "VALUES (" +
                        "?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, ec.getA_Name());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of AccessPrivilege into db failed.");

        return affectedRows;
    }

    public static E_AccessPrivilege retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_AccessPrivilege.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        E_AccessPrivilege ct = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ct = I_AccessPrivillege.FillEntity(rs);
        }

        return ct;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<E_AccessPrivilege> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_AccessPrivilege.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<E_AccessPrivilege> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_AccessPrivillege.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static E_AccessPrivilege FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(E_AccessPrivilege.DBNAME_NAME, rs.getString(E_AccessPrivilege.DBNAME_NAME));

        return  E_AccessPrivilege.CreateFromRetrieved(rs.getInt(E_AccessPrivilege.DBNAME_ID), dict);
    }
}
