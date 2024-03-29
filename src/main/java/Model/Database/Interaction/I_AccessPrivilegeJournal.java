package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_AccessPrivilegeJournal;

import java.sql.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class I_AccessPrivilegeJournal extends InteractionWithDatabase{
    public static int insert(Connection conn, PreparedStatement ps, T_AccessPrivilegeJournal tapj) throws SQLException {
        if (tapj.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_AccessPrivilegeJournal is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                T_AccessPrivilegeJournal.DBNAME_CREATED_AT,
                T_AccessPrivilegeJournal.DBNAME_USER_ID,
                T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID,
                T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID
        );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_AccessPrivilegeJournal.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setDate(++col, tapj.getA_CreatedAt());
        ps.setInt(++col, tapj.getA_UserID());
        ps.setInt(++col, tapj.getA_AccessPrivilegeID());
        ps.setInt(++col, tapj.getA_CreatedByUserID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Access Privilege Journal entry into db failed.");

        return affectedRows;
    }

    public static T_AccessPrivilegeJournal retrieveValidForUser(Connection conn, PreparedStatement ps, ResultSet rs, int userId) throws SQLException {
        Assurance.idCheck(userId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_AccessPrivilegeJournal.DBTABLE_NAME + " " +
                        "WHERE UserID=? ORDER BY ID DESC LIMIT 1"
        );

        int col = 0;
        ps.setInt(++col, userId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_AccessPrivilegeJournal t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = I_AccessPrivilegeJournal.FillEntity(rs);
        }

        return t;
    }

    public static List<T_AccessPrivilegeJournal> retrieveAllSortedByNewest(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_AccessPrivilegeJournal.DBTABLE_NAME + " " +
                        "ORDER BY CreatedAt DESC"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_AccessPrivilegeJournal> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
            return arr;
        }

        // Fill and return filled array
        while (rs.next()) {
            arr.add(I_AccessPrivilegeJournal.FillEntity(rs));
        }

        return arr;
    }

    // Privates
    public static T_AccessPrivilegeJournal FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_AccessPrivilegeJournal.DBNAME_ID, rs.getInt(T_AccessPrivilegeJournal.DBNAME_ID));
        dict.put(T_AccessPrivilegeJournal.DBNAME_CREATED_AT, rs.getDate(T_AccessPrivilegeJournal.DBNAME_CREATED_AT));

        Date deletedAt = rs.getDate(T_AccessPrivilegeJournal.DBNAME_DELETED_AT);
        if (null != deletedAt)
            dict.put(T_AccessPrivilegeJournal.DBNAME_DELETED_AT, deletedAt);

        dict.put(T_AccessPrivilegeJournal.DBNAME_USER_ID, rs.getInt(T_AccessPrivilegeJournal.DBNAME_USER_ID));
        dict.put(T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID, rs.getInt(T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID));
        dict.put(T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID, rs.getInt(T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID));

        return T_AccessPrivilegeJournal.CreateFromRetrieved(rs.getInt(T_AccessPrivilegeJournal.DBNAME_ID), dict);
    }
}
