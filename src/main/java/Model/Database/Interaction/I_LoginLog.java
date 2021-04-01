package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_LoginLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class I_LoginLog {
    public static int insert(Connection conn, PreparedStatement ps, T_LoginLog ec) throws SQLException {
        if (ec.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_LoginLog is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_LoginLog.DBTABLE_NAME + "(" +
                        "LoggedAt, SrcIp, UserID" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setDate(++col, ec.getA_LoggedAt());
        ps.setString(++col, ec.getA_SrcIp());
        ps.setInt(++col, ec.getA_UserId());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of LoginLog into db failed.");

        return affectedRows;
    }

    public static T_LoginLog retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_LoginLog.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_LoginLog ct = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ct = I_LoginLog.FillEntity(rs);
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
    public static List<T_LoginLog> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_LoginLog.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_LoginLog> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_LoginLog.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_LoginLog FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_LoginLog.DBNAME_LOGGEDAT, rs.getDate(T_LoginLog.DBNAME_LOGGEDAT));
        dict.put(T_LoginLog.DBNAME_SRCIP, rs.getString(T_LoginLog.DBNAME_SRCIP));
        dict.put(T_LoginLog.DBNAME_USERID, rs.getInt(T_LoginLog.DBNAME_USERID));

        return T_LoginLog.CreateFromRetrieved(rs.getInt(T_LoginLog.DBNAME_ID), dict);
    }
}
