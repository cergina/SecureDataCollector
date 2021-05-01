package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_ControllerUnit;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_ControllerUnit extends InteractionWithDatabase {

    public static int insert(Connection conn, PreparedStatement ps, T_ControllerUnit tc) throws SQLException {
        if (tc.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_ControllerUnit is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_ControllerUnit.DBNAME_UID, T_ControllerUnit.DBNAME_DIPADDRESS, T_ControllerUnit.DBNAME_ZWAVE, T_ControllerUnit.DBNAME_CENTRALUNIT_ID, T_ControllerUnit.DBNAME_FLAT_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_ControllerUnit.DBTABLE_NAME + "(" +
                        tableNames +
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
        Assurance.idCheck(id);

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


    public static T_ControllerUnit retrieveByUid(Connection conn, PreparedStatement ps, ResultSet rs, int uid) throws SQLException {
        Assurance.idCheck(uid);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                        "WHERE Uid=?"
        );

        int col = 0;
        ps.setInt(++col, uid);

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

    public static T_ControllerUnit retrieveByDipAndCentral(Connection conn, PreparedStatement ps, ResultSet rs, String dip, int id) throws SQLException {
        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                        "WHERE " +
                                T_ControllerUnit.DBNAME_DIPADDRESS + "=? AND " +
                                T_ControllerUnit.DBNAME_CENTRALUNIT_ID + "=? "
        );

        int col = 0;
        ps.setString(++col, dip);
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

    public static List<T_ControllerUnit> retrieveByFlatId(Connection conn, PreparedStatement ps, ResultSet rs, int flatId) throws SQLException {
        Assurance.idCheck(flatId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                        "WHERE " +
                        T_ControllerUnit.DBNAME_FLAT_ID + "=?"
        );

        int col = 0;
        ps.setInt(++col, flatId);

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

    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<T_ControllerUnit> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int centralUnitId, int flatId, String dip) throws SQLException {

        // No Filter is being used
        if (centralUnitId <= DB_DO_NOT_USE_THIS_FILTER && flatId  <= DB_DO_NOT_USE_THIS_FILTER) {
            return InteractionWithDatabase.retrieveAll(conn, ps, rs, DbEntity.ReturnUnusable(T_ControllerUnit.class));
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean centralRule = centralUnitId > 0;
        boolean flatRule = flatId > 0;
        boolean dipRule = StringUtils.isNullOrEmpty(dip) == false;

        /* add first WHERE clause */
        usedSql = (centralRule ? usedSql + T_ControllerUnit.DBTABLE_NAME + ".CentralUnitID=? " : usedSql);

        /* add second WHERE clause - watch out for possibility of AND requirement if first line rule was added */
        usedSql = (flatRule && (centralRule == false) ? usedSql + T_ControllerUnit.DBTABLE_NAME + ".FlatID=? " : usedSql);
        usedSql = (flatRule && centralRule ? usedSql + " AND " + T_ControllerUnit.DBTABLE_NAME + ".FlatID=? " : usedSql);

        /* add second WHERE clause - watch out for possibility of AND requirement if first or second line rule was added */
        usedSql = (dipRule && (centralRule || flatRule) ? usedSql + " AND " + T_ControllerUnit.DBTABLE_NAME + ".DipAddress=? " : usedSql);
        usedSql = (dipRule && (!centralRule  && !flatRule) ? usedSql + T_ControllerUnit.DBTABLE_NAME + ".DipAddress=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (centralRule)
            ps.setInt(++col, centralUnitId);
        if (flatRule)
            ps.setInt(++col, flatId);
        if (dipRule)
            ps.setString(++col, dip);

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
    public static T_ControllerUnit FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_ControllerUnit.DBNAME_UID, rs.getInt(T_ControllerUnit.DBNAME_UID));
        dict.put(T_ControllerUnit.DBNAME_DIPADDRESS, rs.getString(T_ControllerUnit.DBNAME_DIPADDRESS));
        dict.put(T_ControllerUnit.DBNAME_ZWAVE, rs.getString(T_ControllerUnit.DBNAME_ZWAVE));
        dict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, rs.getInt(T_ControllerUnit.DBNAME_CENTRALUNIT_ID));
        dict.put(T_ControllerUnit.DBNAME_FLAT_ID, rs.getInt(T_ControllerUnit.DBNAME_FLAT_ID));

        return T_ControllerUnit.CreateFromRetrieved(rs.getInt(T_ControllerUnit.DBNAME_ID), dict);
    }

    public static boolean checkIfExists(Connection conn, PreparedStatement ps, ResultSet rs, int uid, String dip, int flatId) throws SQLException{
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_ControllerUnit.DBTABLE_NAME + " " +
                        "WHERE Uid=? OR (FlatID=? AND DipAddress=?)"
        );

        int col = 0;
        ps.setInt(++col, uid);
        ps.setInt(++col, flatId);
        ps.setString(++col, dip);


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        if (!rs.isBeforeFirst()) {
            return false;
        }

        return true;
    }

}
