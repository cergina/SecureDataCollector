package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
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

public class I_ControllerUnit {

    public static int insert(Connection conn, PreparedStatement ps, T_ControllerUnit tc) throws SQLException {
        if (tc.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_ControllerUnit is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_ControllerUnit.DBTABLE_NAME + "(" +
                        "Uid, DipAddress, Zwave, CentralUnitID, FlatID" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tc.getA_Uid());
        ps.setString(++col, tc.getA_DipAddress());
        ps.setString(++col, tc.getA_Zwave());
        ps.setInt(++col, tc.getA_CentralUnitID());
        ps.setInt(++col, tc.getA_FlatID());



        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of ControllerUnit into db failed.");

        return affectedRows;
    }

    public static T_ControllerUnit retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_ControllerUnit tc = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tc = I_ControllerUnit.FillEntity(rs);
        }

        return tc;
    }

    /****
     *
     * @param conn
     * @param ps
     * @param rs
     * @param flatId if DB_DO_NOT_USE_THIS_FILTER is passed, it will not be used, otherwise > 0 id has to be entered
     * @param centralUnitId if DB_DO_NOT_USE_THIS_FILTER is passed, it will not be used, otherwise > 0 id has to be entered
     * @return
     * @throws SQLException
     */
    public static List<T_ControllerUnit> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int flatId, int centralUnitId) throws SQLException {

        // No Filter is being used
        if (flatId <= DB_DO_NOT_USE_THIS_FILTER && centralUnitId <= DB_DO_NOT_USE_THIS_FILTER) {
            return retrieveAll(conn, ps, rs);
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean flatRule = flatId > 0;
        boolean centralUnitRule = centralUnitId > 0;

        usedSql = (flatRule ? usedSql + T_ControllerUnit.DBTABLE_NAME + ".FlatID=? " : usedSql);
        usedSql = (centralUnitRule ? usedSql + T_ControllerUnit.DBTABLE_NAME + ".CentralUnitID=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (flatRule)
            ps.setInt(++col, flatId);

        if (centralUnitRule)
            ps.setInt(++col, centralUnitId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_ControllerUnit> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_ControllerUnit.FillEntity(rs));
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
    public static List<T_ControllerUnit> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_ControllerUnit> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_ControllerUnit.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_ControllerUnit FillEntity(ResultSet rs) throws SQLException {
        T_ControllerUnit t = null;

        Dictionary dict = new Hashtable();
        dict.put(T_ControllerUnit.DBNAME_UID, rs.getInt(T_ControllerUnit.DBNAME_UID));
        dict.put(T_ControllerUnit.DBNAME_DIPADDRESS, rs.getString(T_ControllerUnit.DBNAME_DIPADDRESS));
        dict.put(T_ControllerUnit.DBNAME_ZWAVE, rs.getString(T_ControllerUnit.DBNAME_ZWAVE));
        dict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, rs.getInt(T_ControllerUnit.DBNAME_CENTRALUNIT_ID));
        dict.put(T_ControllerUnit.DBNAME_FLAT_ID, rs.getInt(T_ControllerUnit.DBNAME_FLAT_ID));

        t = T_ControllerUnit.CreateFromRetrieved(rs.getInt(T_ControllerUnit.DBNAME_ID), dict);

        return t;
    }
}
