package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_SessionStore;

import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class I_SessionStore {
    public static int insert(Connection conn, PreparedStatement ps, T_SessionStore ec) throws SQLException {
        if (ec.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_SessionStore is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_SessionStore.DBTABLE_NAME + "(" +
                        "Session, Data, CreatedAt" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, ec.getA_Session());
        ps.setString(++col, ec.getA_Data());
        ps.setDate(++col, ec.getA_CreatedAt());


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of SessionStore into db failed.");

        return affectedRows;
    }

    public static T_SessionStore retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_SessionStore.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_SessionStore ct = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ct = I_SessionStore.FillEntity(rs);
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
    public static List<T_SessionStore> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_SessionStore.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_SessionStore> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_SessionStore.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_SessionStore FillEntity(ResultSet rs) throws SQLException {
        T_SessionStore e = null;

        Dictionary dict = new Hashtable();
        dict.put(T_SessionStore.DBNAME_SESSION, rs.getString(T_SessionStore.DBNAME_SESSION));
        dict.put(T_SessionStore.DBNAME_DATA, rs.getString(T_SessionStore.DBNAME_DATA));
        dict.put(T_SessionStore.DBNAME_CREATEDAT, rs.getDate(T_SessionStore.DBNAME_CREATEDAT));

        Date deletedAt = rs.getDate(T_SessionStore.DBNAME_DELETEDAT);
        if (null != deletedAt)
            dict.put(T_SessionStore.DBNAME_DELETEDAT, deletedAt);

        e = T_SessionStore.CreateFromRetrieved(rs.getInt(T_SessionStore.DBNAME_ID), dict);

        return e;
    }
}
