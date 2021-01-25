package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_Address;
import Database.Tables.T_Flat;
import Database.Tables.T_User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Flat {
    public static int insert(Connection conn, PreparedStatement ps, T_Flat tf) throws SQLException {
        if (tf.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Flat is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Flat.DBTABLE_NAME + "(" +
                        "ApartmentNO, AddressID" +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tf.getA_ApartmentNO());
        ps.setInt(++col, tf.getA_AddressID());

        // SQL Execution
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Flat into db failed.");

        return affectedRows;
    }

    public static T_Flat retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Flat.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        rs = ps.executeQuery();
        T_Flat tf = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tf = Flat.FillEntity(rs);
        }

        return tf;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_Flat> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Flat.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        rs = ps.executeQuery();

        ArrayList<T_Flat> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(Flat.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_Flat FillEntity(ResultSet rs) throws SQLException {
        T_Flat t = null;

        Dictionary dict = new Hashtable();
        dict.put(T_Flat.DBNAME_APARTMENTNO, rs.getString(T_Flat.DBNAME_APARTMENTNO));
        dict.put(T_Flat.DBNAME_ADDRESS_ID, rs.getInt(T_Flat.DBNAME_ADDRESS_ID));

        t = T_Flat.CreateFromRetrieved(rs.getInt(T_Flat.DBNAME_ID), dict);

        return t;
    }
}
