package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.Table.T_Measurement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_Measurements {

    public static int insert(Connection conn, PreparedStatement ps, T_Measurement tm) throws SQLException {
        if (tm.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Measurements is not ok for database enter");

        // Find out accumulated value from DB
        ResultSet rs = null;
        T_Measurement tm_recentInDb = retrieveNewest(conn, ps, rs, tm.getA_SensorID());
        int newAccumulated = (tm_recentInDb == null) ? tm.getA_Value() : tm.getA_Value() + tm_recentInDb.getA_AccumulatedValue();

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Measurement.DBTABLE_NAME + "(" +
                        "Value, RequestNo, MeasuredAt, AccumulatedValue,SensorID" +
                        ") " +
                        "VALUES ( " +
                        "?, " + // value
                        "?, " + // reqNo
                        "?, " + // MeasuredAt
                        "?, " + // AccumValue
                        "?" + // SensorId
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tm.getA_Value());
        ps.setInt(++col, tm.getA_RequestNo());
        ps.setDate(++col, tm.getA_MeasuredAt());
        ps.setInt(++col, newAccumulated);
        ps.setInt(++col, tm.getA_SensorID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Measurements into db failed.");

        return affectedRows;
    }

    public static T_Measurement retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Measurement.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Measurement tm = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tm = I_Measurements.FillEntity(rs);
        }

        return tm;
    }

    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @param sensorId if DB_DO_NOT_USE_THIS_FILTER is passed, it will not be used, otherwise > 0 id has to be entered
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_Measurement> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int sensorId) throws SQLException {

        // No Filter is being used
        if (sensorId <= DB_DO_NOT_USE_THIS_FILTER) {
            return retrieveAll(conn, ps, rs);
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_Measurement.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean sensorRule = sensorId > 0;

        usedSql = (sensorRule ? usedSql + T_Measurement.DBTABLE_NAME + ".SensorID=? " : usedSql);

        usedSql += "ORDER BY ID asc";

        // prepare SQL
        ps = conn.prepareStatement(
                usedSql
        );

        int col = 0;
        if (sensorRule)
            ps.setInt(++col, sensorId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<T_Measurement> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Measurements.FillEntity(rs));
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
    public static ArrayList<T_Measurement> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Measurement.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<T_Measurement> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Measurements.FillEntity(rs));
            }
        }

        return arr;
    }

    public static T_Measurement retrieveNewest(Connection conn, PreparedStatement ps, ResultSet rs, int sensorId) throws SQLException {
        Assurance.IdCheck(sensorId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Measurement.DBTABLE_NAME + " " +
                        "WHERE SensorID=? ORDER BY AccumulatedValue DESC LIMIT 1"
        );

        int col = 0;
        ps.setInt(++col, sensorId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Measurement tm = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tm = I_Measurements.FillEntity(rs);
        }

        return tm;
    }

    public static int measuredLast30DaysForSensor(Connection conn, PreparedStatement ps, ResultSet rs, int sensorId) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "SUM(Value) " +
                        "FROM " + T_Measurement.DBTABLE_NAME + " " +
                        "WHERE MeasuredAt > ? AND SensorID = ? GROUP BY SensorID ORDER BY ID asc"
        );

        int col = 0;
        Date date = Date.valueOf(LocalDate.now().minusDays(30));
        ps.setDate(++col, date);
        ps.setInt(++col, sensorId);


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        int sum = -1;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                sum = rs.getInt(1);
            }
        }

        return sum;
    }


    // Privates
    private static T_Measurement FillEntity(ResultSet rs) throws SQLException {
        T_Measurement t = null;

        Dictionary dict = new Hashtable();
        dict.put(T_Measurement.DBNAME_VALUE, rs.getInt(T_Measurement.DBNAME_VALUE));
        dict.put(T_Measurement.DBNAME_REQUESTNO, rs.getInt(T_Measurement.DBNAME_REQUESTNO));
        dict.put(T_Measurement.DBNAME_MEASUREDAT, rs.getDate(T_Measurement.DBNAME_MEASUREDAT));
        dict.put(T_Measurement.DBNAME_ACCUMULATEDVALUE, rs.getInt(T_Measurement.DBNAME_ACCUMULATEDVALUE));
        dict.put(T_Measurement.DBNAME_SENSOR_ID, rs.getInt(T_Measurement.DBNAME_SENSOR_ID));

        t = T_Measurement.CreateFromRetrieved(rs.getInt(T_Measurement.DBNAME_ID), dict);

        return t;
    }
}
