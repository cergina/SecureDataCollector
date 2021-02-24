package Database.Interaction.Entities;

import Database.Enums.E_CommType;
import Database.Support.Assurance;
import Database.Support.SqlConnectionOneTimeReestablisher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class CommType {
    public static int insert(Connection conn, PreparedStatement ps, E_CommType ec) throws SQLException {
        if (ec.IsEnumTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute E_CommType is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        E_CommType.DBTABLE_NAME + "(" +
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
            throw new SQLException("Something happened. Insertion of CommType into db failed.");

        return affectedRows;
    }

    public static E_CommType retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_CommType.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        E_CommType ct = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ct = CommType.FillEntity(rs);
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
    public static ArrayList<E_CommType> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_CommType.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<E_CommType> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(CommType.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static E_CommType FillEntity(ResultSet rs) throws SQLException {
        E_CommType e = null;

        Dictionary dict = new Hashtable();
        dict.put(E_CommType.DBNAME_NAME, rs.getString(E_CommType.DBNAME_NAME));

        e = E_CommType.CreateFromRetrieved(rs.getInt(E_CommType.DBNAME_ID), dict);

        return e;
    }
}
