package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_User extends InteractionWithDatabase {

    public static int insert(Connection conn, PreparedStatement ps, T_User tu) throws SQLException {
        if (tu.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_User is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_User.DBNAME_BEFORETITLE, T_User.DBNAME_FIRSTNAME, T_User.DBNAME_MIDDLENAME, T_User.DBNAME_LASTNAME, T_User.DBNAME_PHONE, T_User.DBNAME_EMAIL, T_User.DBNAME_PERMANENTRESIDENCE
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_User.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?, ?, ?" +
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

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of User into db failed.");

        return affectedRows;
    }

    public static T_User retrieveByEmail(Connection conn, PreparedStatement ps, ResultSet rs, String email) throws SQLException {
        Assurance.varcharCheck(email);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_User.DBTABLE_NAME + " " +
                        "WHERE Email=?"
        );

        int col = 0;
        ps.setString(++col, email);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_User t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = I_User.FillEntity(rs);
        }

        return t;
    }


    // Privates
    public static T_User FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_User.DBNAME_BEFORETITLE, rs.getString(T_User.DBNAME_BEFORETITLE));
        dict.put(T_User.DBNAME_FIRSTNAME, rs.getString(T_User.DBNAME_FIRSTNAME));
        dict.put(T_User.DBNAME_MIDDLENAME, rs.getString(T_User.DBNAME_MIDDLENAME));
        dict.put(T_User.DBNAME_LASTNAME, rs.getString(T_User.DBNAME_LASTNAME));
        dict.put(T_User.DBNAME_PHONE, rs.getString(T_User.DBNAME_PHONE));
        dict.put(T_User.DBNAME_EMAIL, rs.getString(T_User.DBNAME_EMAIL));
        dict.put(T_User.DBNAME_PERMANENTRESIDENCE, rs.getString(T_User.DBNAME_PERMANENTRESIDENCE));
        dict.put(T_User.DBNAME_BLOCKED, rs.getInt(T_User.DBNAME_BLOCKED));

        return T_User.CreateFromRetrieved(rs.getInt(T_User.DBNAME_ID), dict);
    }


}
