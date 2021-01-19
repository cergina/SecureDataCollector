package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_Address;
import Database.Tables.T_User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Address {
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

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Address.DBTABLE_NAME + "(" +
                        "Country, City, Street, HouseNO, Zip" +
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
        int affectedRows = ps.executeUpdate();

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
        Assurance.IdCheck(id);

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
        rs = ps.executeQuery();
        T_Address ta = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ta = Address.FillEntity(rs);
        }

        return ta;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_Address> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Address.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        rs = ps.executeQuery();

        ArrayList<T_Address> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(Address.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_Address FillEntity(ResultSet rs) throws SQLException {
        T_Address t = null;

        Dictionary dict = new Hashtable();

        dict.put(T_Address.DBNAME_COUNTRY, rs.getString(T_Address.DBNAME_COUNTRY));
        dict.put(T_Address.DBNAME_CITY, rs.getString(T_Address.DBNAME_CITY));
        dict.put(T_Address.DBNAME_STREET, rs.getString(T_Address.DBNAME_STREET));
        dict.put(T_Address.DBNAME_HOUSENO, rs.getString(T_Address.DBNAME_HOUSENO));
        dict.put(T_Address.DBNAME_ZIP, rs.getString(T_Address.DBNAME_ZIP));

        t = T_Address.CreateFromRetrieved(rs.getInt(T_Address.DBNAME_ID), dict);

        return t;
    }


}
