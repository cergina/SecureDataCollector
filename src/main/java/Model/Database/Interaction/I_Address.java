package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Address;
import Model.Database.Tables.T_Building;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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
    public static List<T_Address> retrieveUnusedAddresses(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {

        // SQL Definition
        String usedSql = "SELECT " +
                "t1.* " +
                "FROM " + T_Address.DBTABLE_NAME + " t1 " +
                "LEFT JOIN " + T_Building.DBTABLE_NAME + " t2 ON t1.ID=t2.AddressID " +
                "WHERE t2.AddressID IS NULL";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Address> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Address.FillEntity(rs));
            }
        }

        return arr;
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

        return rs.isBeforeFirst();
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

}
