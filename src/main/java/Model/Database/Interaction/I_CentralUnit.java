package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_CentralUnit;
import Model.Database.Tables.Table.T_ControllerUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_CentralUnit {
    /****
     * Attempt to insert the parsed argument T_CentralUnit into the real database
     * @param conn
     * @param ps
     * @param tcu
     * @return
     * @throws SQLException
     */
    public static int insert(Connection conn, PreparedStatement ps, T_CentralUnit tcu) throws SQLException {
        if (tcu.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_CentralUnit is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                T_CentralUnit.DBNAME_UID,
                T_CentralUnit.DBNAME_DIPADDRESS,
                T_CentralUnit.DBNAME_FRIENDLYNAME,
                T_CentralUnit.DBNAME_SIMNO,
                T_CentralUnit.DBNAME_IMEI,
                T_CentralUnit.DBNAME_ZWAVE,
                T_CentralUnit.DBNAME_PROJECT_ID,
                T_CentralUnit.DBNAME_ADDRESS_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_CentralUnit.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tcu.getA_Uid());
        ps.setString(++col, tcu.getA_DipAddress());
        ps.setString(++col, tcu.getA_FriendlyName());
        ps.setString(++col, tcu.getA_SimNO());
        ps.setString(++col, tcu.getA_Imei());
        ps.setString(++col, tcu.getA_Zwave());
        ps.setInt(++col, tcu.getA_ProjectID());
        ps.setInt(++col, tcu.getA_AddressID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of CentralUnit into db failed.");

        return affectedRows;
    }

    /****
     *
     * @param conn
     * @param ps
     * @param rs
     * @param id
     * @return
     * @throws SQLException
     */
    public static T_CentralUnit retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_CentralUnit.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_CentralUnit tc = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tc = I_CentralUnit.FillEntity(rs);
        }

        return tc;
    }

    public static List<T_CentralUnit> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int projectId) throws SQLException {

        // No Filter is being used
        if (projectId <= DB_DO_NOT_USE_THIS_FILTER) {
            return retrieveAll(conn, ps, rs);
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_CentralUnit.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean projectRule = projectId > 0;

        usedSql = (projectRule ? usedSql + T_CentralUnit.DBTABLE_NAME + ".ProjectID=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (projectRule)
            ps.setInt(++col, projectId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_CentralUnit> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_CentralUnit.FillEntity(rs));
            }
        }

        return arr;
    }

    public static T_CentralUnit retrieveByAddressId(Connection conn, PreparedStatement ps, ResultSet rs, int addressId) throws SQLException {
        Assurance.idCheck(addressId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_CentralUnit.DBTABLE_NAME + " " +
                        "WHERE " + T_CentralUnit.DBNAME_ADDRESS_ID +"=?"
        );

        int col = 0;
        ps.setInt(++col, addressId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_CentralUnit tc = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tc = I_CentralUnit.FillEntity(rs);
        }

        return tc;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<T_CentralUnit> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_CentralUnit.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_CentralUnit> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_CentralUnit.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_CentralUnit FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_CentralUnit.DBNAME_UID, rs.getInt(T_CentralUnit.DBNAME_UID));
        dict.put(T_CentralUnit.DBNAME_DIPADDRESS, rs.getString(T_CentralUnit.DBNAME_DIPADDRESS));
        dict.put(T_CentralUnit.DBNAME_FRIENDLYNAME, rs.getString(T_CentralUnit.DBNAME_FRIENDLYNAME));
        dict.put(T_CentralUnit.DBNAME_SIMNO, rs.getString(T_CentralUnit.DBNAME_SIMNO));
        dict.put(T_CentralUnit.DBNAME_IMEI, rs.getString(T_CentralUnit.DBNAME_IMEI));
        dict.put(T_CentralUnit.DBNAME_ZWAVE, rs.getString(T_CentralUnit.DBNAME_ZWAVE));
        dict.put(T_CentralUnit.DBNAME_PROJECT_ID, rs.getInt(T_CentralUnit.DBNAME_PROJECT_ID));
        dict.put(T_CentralUnit.DBNAME_ADDRESS_ID, rs.getInt(T_CentralUnit.DBNAME_ADDRESS_ID));

        return T_CentralUnit.CreateFromRetrieved(rs.getInt(T_CentralUnit.DBNAME_ID), dict);
    }
}
