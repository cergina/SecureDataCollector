package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_Hash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_Hash {
    public static int insert(Connection conn, PreparedStatement ps, T_Hash th) throws SQLException {
        if (th.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Hash is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Hash.DBTABLE_NAME + "(" +
                        "Value, UserID" +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, th.getA_Value());
        ps.setInt(++col, th.getA_UserID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Hash into db failed.");

        return affectedRows;
    }

    public static T_Hash retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Hash.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Hash t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = I_Hash.FillEntity(rs);
        }

        return t;
    }

    public static T_Hash retrieveCode(Connection conn, PreparedStatement ps, ResultSet rs, int userID) throws SQLException {
        Assurance.IdCheck(userID);

        ps = conn.prepareStatement(
                "SELECT * FROM " + T_Hash.DBTABLE_NAME + " WHERE UserID=? GROUP BY UserID ORDER BY ID ASC LIMIT 1"
        );

        int col = 0;
        ps.setInt(++col, userID);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Hash t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = I_Hash.FillEntity(rs);
        }

        return t;
    }

    public static T_Hash retrieveLatest(Connection conn, PreparedStatement ps, ResultSet rs, int userID) throws SQLException {
        Assurance.IdCheck(userID);

        ps = conn.prepareStatement(
                "SELECT * FROM " + T_Hash.DBTABLE_NAME + " WHERE UserID=? ORDER BY ID DESC LIMIT 1"
        );

        int col = 0;
        ps.setInt(++col, userID);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Hash t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();
            t = I_Hash.FillEntity(rs);
        }

        return t;
    }

    public static int countHashesForUser(Connection conn, PreparedStatement ps, ResultSet rs, int userID) throws SQLException {
        Assurance.IdCheck(userID);

        ps = conn.prepareStatement(
                "SELECT COUNT(*) AS numHashes FROM " + T_Hash.DBTABLE_NAME + " WHERE UserID=? GROUP BY UserID"
        );

        int col = 0;
        ps.setInt(++col, userID);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        int numHashes = 0;
        while (rs.next()) {
            numHashes = rs.getInt(1);
        }
        return numHashes;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_Hash> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Hash.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<T_Hash> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Hash.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_Hash FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Hash.DBNAME_VALUE, rs.getString(T_Hash.DBNAME_VALUE));
        dict.put(T_Hash.DBNAME_USER_ID, rs.getInt(T_Hash.DBNAME_USER_ID));

        return T_Hash.CreateFromRetrieved(rs.getInt(T_Hash.DBNAME_ID), dict);
    }


}
