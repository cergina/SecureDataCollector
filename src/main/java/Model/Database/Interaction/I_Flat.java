package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_CentralUnit;
import Model.Database.Tables.Table.T_Flat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_Flat {
    public static int insert(Connection conn, PreparedStatement ps, T_Flat tf) throws SQLException {
        if (tf.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Flat is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_Flat.DBNAME_APARTMENTNO, T_Flat.DBNAME_ADDRESS_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Flat.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tf.getA_ApartmentNO());
        ps.setInt(++col, tf.getA_AddressID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Flat into db failed.");

        return affectedRows;
    }

    public static T_Flat retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.idCheck(id);

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
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Flat tf = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tf = I_Flat.FillEntity(rs);
        }

        return tf;
    }

    /*
     * This will get you back the PRIMARY KEY value of the last row that you inserted, because it's per connection !
     */
    public static int retrieveLatestPerConnectionInsertedID(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        int latest  = -1;

        ps = conn.prepareStatement(
                "SELECT LAST_INSERT_ID();"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            latest = rs.getInt(1);
        }

        return latest;
    }

    public static List<T_Flat> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int addressId) throws SQLException {

        // No Filter is being used
        if (addressId <= DB_DO_NOT_USE_THIS_FILTER) {
            return retrieveAll(conn, ps, rs);
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_Flat.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean projectRule = addressId > 0;

        usedSql = (projectRule ? usedSql + T_Flat.DBTABLE_NAME + ".AddressID=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (projectRule)
            ps.setInt(++col, addressId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Flat> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Flat.FillEntity(rs));
            }
        }

        return arr;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<T_Flat> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Flat.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Flat> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Flat.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_Flat FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Flat.DBNAME_APARTMENTNO, rs.getString(T_Flat.DBNAME_APARTMENTNO));
        dict.put(T_Flat.DBNAME_ADDRESS_ID, rs.getInt(T_Flat.DBNAME_ADDRESS_ID));

        return T_Flat.CreateFromRetrieved(rs.getInt(T_Flat.DBNAME_ID), dict);
    }
}
