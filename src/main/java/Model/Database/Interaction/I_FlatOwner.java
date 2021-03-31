package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_FlatOwner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_FlatOwner {

    public static int insert(Connection conn, PreparedStatement ps, T_FlatOwner tfo) throws SQLException {
        if (tfo.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_FlatOwner is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_FlatOwner.DBTABLE_NAME + "(" +
                        "BeforeTitle, FirstName, MiddleName, LastName, Phone, Email, Address" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tfo.getA_BeforeTitle());
        ps.setString(++col, tfo.getA_FirstName());
        ps.setString(++col, tfo.getA_MiddleName());
        ps.setString(++col, tfo.getA_LastName());
        ps.setString(++col, tfo.getA_Phone());
        ps.setString(++col, tfo.getA_Email());
        ps.setString(++col, tfo.getA_Address());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of FlatOwner into db failed.");

        return affectedRows;
    }

    public static T_FlatOwner retrieveByAdditionalArguments(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_FlatOwner.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_FlatOwner t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = FillEntity(rs);
        }

        return t;
    }

    public static T_FlatOwner retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_FlatOwner.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_FlatOwner t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = FillEntity(rs);
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
    public static ArrayList<T_FlatOwner> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_FlatOwner.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<T_FlatOwner> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(FillEntity(rs));
            }
        }

        return arr;
    }


    // Privates
    private static T_FlatOwner FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_FlatOwner.DBNAME_BEFORETITLE, rs.getString(T_FlatOwner.DBNAME_BEFORETITLE));
        dict.put(T_FlatOwner.DBNAME_FIRSTNAME, rs.getString(T_FlatOwner.DBNAME_FIRSTNAME));
        dict.put(T_FlatOwner.DBNAME_MIDDLENAME, rs.getString(T_FlatOwner.DBNAME_MIDDLENAME));
        dict.put(T_FlatOwner.DBNAME_LASTNAME, rs.getString(T_FlatOwner.DBNAME_LASTNAME));
        dict.put(T_FlatOwner.DBNAME_PHONE, rs.getString(T_FlatOwner.DBNAME_PHONE));
        dict.put(T_FlatOwner.DBNAME_EMAIL, rs.getString(T_FlatOwner.DBNAME_EMAIL));
        dict.put(T_FlatOwner.DBNAME_ADDRESS, rs.getString(T_FlatOwner.DBNAME_ADDRESS));

        return T_FlatOwner.CreateFromRetrieved(rs.getInt(T_FlatOwner.DBNAME_ID), dict);
    }
}
