package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_SessionStore;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_SessionStore extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, T_SessionStore ec) throws SQLException {
        if (ec.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_SessionStore is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_SessionStore.DBNAME_SESSION, T_SessionStore.DBNAME_DATA, T_SessionStore.DBNAME_CREATEDAT
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_SessionStore.DBTABLE_NAME + "(" +
                        tableNames +
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
        Assurance.idCheck(id);

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

    // Privates
    public static T_SessionStore FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_SessionStore.DBNAME_SESSION, rs.getString(T_SessionStore.DBNAME_SESSION));
        dict.put(T_SessionStore.DBNAME_DATA, rs.getString(T_SessionStore.DBNAME_DATA));
        dict.put(T_SessionStore.DBNAME_CREATEDAT, rs.getDate(T_SessionStore.DBNAME_CREATEDAT));

        Date deletedAt = rs.getDate(T_SessionStore.DBNAME_DELETEDAT);
        if (null != deletedAt)
            dict.put(T_SessionStore.DBNAME_DELETEDAT, deletedAt);

        return T_SessionStore.CreateFromRetrieved(rs.getInt(T_SessionStore.DBNAME_ID), dict);
    }
}
