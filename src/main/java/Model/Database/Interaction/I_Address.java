package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_Address extends InteractionWithDatabase {

    /***
     * Attempt to insert the parsed argument T_Address into the real database
     * @param conn
     * @param ps
     * @param ta
     * @throws SQLException
     */
    public static int insert(Connection conn, PreparedStatement ps, T_Address ta) throws SQLException {
        if (ta.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Address is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                T_Address.DBNAME_COUNTRY, T_Address.DBNAME_CITY, T_Address.DBNAME_STREET, T_Address.DBNAME_HOUSENO, T_Address.DBNAME_ZIP
        );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Address.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, ta.getA_Country());
        ps.setString(++col, ta.getA_City());
        ps.setString(++col, ta.getA_Street());
        ps.setString(++col, ta.getA_HouseNO());
        ps.setString(++col, ta.getA_ZIP());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of address into db failed.");

        return affectedRows;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @param id
     * @return
     * @throws SQLException
     */
    public static T_Address retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "ID, Country, City, Street, HouseNO, Zip " +
                        "FROM " + T_Address.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Address ta = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ta = I_Address.FillEntity(rs);
        }

        return ta;
    }

    // Privates
    public static T_Address FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Address.DBNAME_COUNTRY, rs.getString(T_Address.DBNAME_COUNTRY));
        dict.put(T_Address.DBNAME_CITY, rs.getString(T_Address.DBNAME_CITY));
        dict.put(T_Address.DBNAME_STREET, rs.getString(T_Address.DBNAME_STREET));
        dict.put(T_Address.DBNAME_HOUSENO, rs.getString(T_Address.DBNAME_HOUSENO));
        dict.put(T_Address.DBNAME_ZIP, rs.getString(T_Address.DBNAME_ZIP));

        return T_Address.CreateFromRetrieved(rs.getInt(T_Address.DBNAME_ID), dict);
    }

    public static boolean checkIfExists(Connection conn, PreparedStatement ps, ResultSet rs, String street, String houseno, String city, String zip, String country) throws SQLException {
        Assurance.varcharCheck(street);
        Assurance.varcharCheck(houseno);
        Assurance.varcharCheck(city);
        Assurance.varcharCheck(zip);
        Assurance.varcharCheck(country);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Address.DBTABLE_NAME + " " +
                        "WHERE Street=? AND HouseNO=? AND City=? AND Zip=? AND Country=?"
        );

        int col = 0;
        ps.setString(++col, street);
        ps.setString(++col, houseno);
        ps.setString(++col, city);
        ps.setString(++col, zip);
        ps.setString(++col, country);


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        if (!rs.isBeforeFirst()) {
            return false;
        }

        return true;
    }

}
