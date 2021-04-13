package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_Sensor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_Sensor {

    public static int insert(Connection conn, PreparedStatement ps, T_Sensor ts) throws SQLException {
        if (ts.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Sensor is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_Sensor.DBNAME_INPUT, T_Sensor.DBNAME_NAME, T_Sensor.DBNAME_SENSORTYPE_ID, T_Sensor.DBNAME_CONTROLLERUNIT_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Sensor.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, ts.getA_Input());
        ps.setString(++col, ts.getA_Name());
        ps.setInt(++col, ts.getA_SensorTypeID());
        ps.setInt(++col, ts.getA_ControllerUnitID());


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Sensor into db failed.");

        return affectedRows;
    }

    public static T_Sensor retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Sensor.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Sensor ts = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ts = I_Sensor.FillEntity(rs);
        }

        return ts;
    }

    public static T_Sensor retrieveByInputAndControllerUnitId(Connection conn, PreparedStatement ps, ResultSet rs, String input, int cid) throws SQLException {
        Assurance.varcharCheck(input);
        Assurance.idCheck(cid);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Sensor.DBTABLE_NAME + " " +
                        "WHERE " + T_Sensor.DBNAME_INPUT + "=? AND "
                        + T_Sensor.DBNAME_CONTROLLERUNIT_ID + "=?"
        );

        int col = 0;
        ps.setString(++col, input);
        ps.setInt(++col, cid);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Sensor ts = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            ts = I_Sensor.FillEntity(rs);
        }

        return ts;
    }

    ////////////////////////
    ////     Searches   ////
    ///////////////////////

    public static int retrieve_ID_by_SensorIO(Connection conn, PreparedStatement ps, ResultSet rs, String sensorIO) throws SQLException {
        Assurance.varcharCheck(sensorIO);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "ID " +
                        "FROM " + T_Sensor.DBTABLE_NAME + " " +
                        "WHERE Input=?"
        );

        int col = 0;
        ps.setString(++col, sensorIO);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Sensor ts = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            return rs.getInt(T_Sensor.DBNAME_ID);
        }

        throw new SQLException("No such sensor SensorIO" + sensorIO + " in database");
    }

    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @param sensorTypeId if DB_DO_NOT_USE_THIS_FILTER is passed, it will not be used, otherwise > 0 id has to be entered
     * @param controllerUnitId if DB_DO_NOT_USE_THIS_FILTER is passed, it will not be used, otherwise > 0 id has to be entered
     * @return
     * @throws SQLException
     */
    public static List<T_Sensor> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int sensorTypeId, int controllerUnitId) throws SQLException {

        // No Filter is being used
        if (sensorTypeId <= DB_DO_NOT_USE_THIS_FILTER && controllerUnitId <= DB_DO_NOT_USE_THIS_FILTER) {
            return retrieveAll(conn, ps, rs);
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_Sensor.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean sensorTypeRule = sensorTypeId > 0;
        boolean controllerUnitRule = controllerUnitId > 0;

        usedSql = (sensorTypeRule ? usedSql + T_Sensor.DBTABLE_NAME + ".SensorTypeID=? " : usedSql);
        usedSql = (controllerUnitRule ? usedSql + T_Sensor.DBTABLE_NAME + ".ControllerUnitID=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (sensorTypeRule)
            ps.setInt(++col, sensorTypeId);

        if (controllerUnitRule)
            ps.setInt(++col, controllerUnitId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Sensor> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Sensor.FillEntity(rs));
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
    public static List<T_Sensor> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Sensor.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Sensor> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Sensor.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_Sensor FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Sensor.DBNAME_INPUT, rs.getString(T_Sensor.DBNAME_INPUT));
        dict.put(T_Sensor.DBNAME_NAME, rs.getString(T_Sensor.DBNAME_NAME));
        dict.put(T_Sensor.DBNAME_SENSORTYPE_ID, rs.getInt(T_Sensor.DBNAME_SENSORTYPE_ID));
        dict.put(T_Sensor.DBNAME_CONTROLLERUNIT_ID, rs.getInt(T_Sensor.DBNAME_CONTROLLERUNIT_ID));

        return T_Sensor.CreateFromRetrieved(rs.getInt(T_Sensor.DBNAME_ID), dict);
    }
}
