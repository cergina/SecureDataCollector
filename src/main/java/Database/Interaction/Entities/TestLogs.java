package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_TestLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class TestLogs {
    public static int insert(Connection conn, PreparedStatement ps, T_TestLog tt) throws SQLException {
        if (tt.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_TestLog is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_TestLog.DBTABLE_NAME + "(" +
                        "Event, Body" +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tt.getA_Event());
        ps.setString(++col, tt.getA_Body());


        // SQL Execution
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of TestLog into db failed.");

        return affectedRows;
    }

    public static T_TestLog retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_TestLog.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        rs = ps.executeQuery();
        T_TestLog tt = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tt = TestLogs.FillEntity(rs);
        }

        return tt;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_TestLog> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_TestLog.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        rs = ps.executeQuery();

        ArrayList<T_TestLog> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(TestLogs.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_TestLog FillEntity(ResultSet rs) throws SQLException {
        T_TestLog t = null;

        Dictionary dict = new Hashtable();
        dict.put(T_TestLog.DBNAME_EVENT, rs.getString(T_TestLog.DBNAME_EVENT));
        dict.put(T_TestLog.DBNAME_BODY, rs.getString(T_TestLog.DBNAME_BODY));

        t = T_TestLog.CreateFromRetrieved(rs.getInt(T_TestLog.DBNAME_ID), dict);

        return t;
    }
}
