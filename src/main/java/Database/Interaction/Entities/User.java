package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Support.SqlConnectionOneTimeReestablisher;
import Database.Tables.T_User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class User {

    public static int insert(Connection conn, PreparedStatement ps, T_User tu) throws SQLException {
        if (tu.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_User is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_User.DBTABLE_NAME + "(" +
                        "BeforeTitle, FirstName, MiddleName, LastName, Phone, Email, PermanentResidence, Blocked" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tu.getA_BeforeTitle());
        ps.setString(++col, tu.getA_FirstName());
        ps.setString(++col, tu.getA_MiddleName());
        ps.setString(++col, tu.getA_LastName());
        ps.setString(++col, tu.getA_Phone());
        ps.setString(++col, tu.getA_Email());
        ps.setString(++col, tu.getA_PermanentResidence());
        ps.setInt(++col, tu.isA_Blocked_Numerical());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of User into db failed.");

        return affectedRows;
    }

    public static T_User retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_User.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_User t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = User.FillEntity(rs);
        }

        return t;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_User> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_User.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<T_User> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(User.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_User FillEntity(ResultSet rs) throws SQLException {
        T_User t = null;

        Dictionary dict = new Hashtable();

        dict.put(T_User.DBNAME_BEFORETITLE, rs.getString(T_User.DBNAME_BEFORETITLE));
        dict.put(T_User.DBNAME_FIRSTNAME, rs.getString(T_User.DBNAME_FIRSTNAME));
        dict.put(T_User.DBNAME_MIDDLENAME, rs.getString(T_User.DBNAME_MIDDLENAME));
        dict.put(T_User.DBNAME_LASTNAME, rs.getString(T_User.DBNAME_LASTNAME));
        dict.put(T_User.DBNAME_PHONE, rs.getString(T_User.DBNAME_PHONE));
        dict.put(T_User.DBNAME_EMAIL, rs.getString(T_User.DBNAME_EMAIL));
        dict.put(T_User.DBNAME_PERMANENTRESIDENCE, rs.getString(T_User.DBNAME_PERMANENTRESIDENCE));
        dict.put(T_User.DBNAME_BLOCKED, rs.getInt(T_User.DBNAME_BLOCKED));

        t = T_User.CreateFromRetrieved(rs.getInt(T_User.DBNAME_ID), dict);

        return t;
    }


}
