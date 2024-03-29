package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.DbEntity;
import Model.Database.Tables.T_Measurement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static Model.Database.Support.DbConfig.DB_DO_NOT_USE_THIS_FILTER;

public class I_Measurements extends InteractionWithDatabase {

    public static int insert(Connection conn, PreparedStatement ps, T_Measurement tm) throws SQLException {
        if (tm.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Measurements is not ok for database enter");

        // Find out accumulated value from DB
        ResultSet rs = null;
        T_Measurement tm_recentInDb = retrieveNewest(conn, ps, rs, tm.getA_SensorID());
        int newAccumulated = (tm_recentInDb == null) ? tm.getA_Value() : tm.getA_Value() + tm_recentInDb.getA_AccumulatedValue();

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_Measurement.DBNAME_VALUE, T_Measurement.DBNAME_REQUESTNO, T_Measurement.DBNAME_MEASUREDAT, T_Measurement.DBNAME_ACCUMULATEDVALUE, T_Measurement.DBNAME_SENSOR_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Measurement.DBTABLE_NAME + "(" +
                        tableNames +
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


    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @param sensorId if DB_DO_NOT_USE_THIS_FILTER is passed, it will not be used, otherwise > 0 id has to be entered
     * @return
     * @throws SQLException
     */
    public static List<T_Measurement> retrieveFilteredAll(Connection conn, PreparedStatement ps, ResultSet rs, int sensorId) throws SQLException {

        // No Filter is being used
        if (sensorId <= DB_DO_NOT_USE_THIS_FILTER) {
            return InteractionWithDatabase.retrieveAll(conn, ps, rs, DbEntity.ReturnUnusable(T_Measurement.class));
        }

        // SQL Definition
        String usedSql = "SELECT " +
                "* " +
                "FROM " + T_Measurement.DBTABLE_NAME + " " +
                "WHERE ";


        // add filter rules
        boolean sensorRule = sensorId > 0;

        usedSql = (sensorRule ? usedSql + T_Measurement.DBTABLE_NAME + ".SensorID=? " : usedSql);

        usedSql += " GROUP BY RequestNo ORDER BY RequestNo asc";

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

        List<T_Measurement> arr = new ArrayList<>();

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
        Assurance.idCheck(sensorId);

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

    /***
     *
     * @param conn
     * @param ps
     * @param rs
     * @param sensorId
     * @return -1 when no sum is measured || some actual value
     * @throws SQLException
     */
    public static int measuredLast30DaysForSensor(Connection conn, PreparedStatement ps, ResultSet rs, int sensorId) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "SUM(Value) " +
                        "FROM " + T_Measurement.DBTABLE_NAME + " " +
                        "WHERE MeasuredAt > ? AND SensorID = ? GROUP BY SensorID, ID ORDER BY ID asc"
        );

        int col = 0;
        Date date = Date.valueOf(LocalDate.now().minusDays(30));
        ps.setDate(++col, date);
        ps.setInt(++col, sensorId);


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        int sum = 0;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                sum += rs.getInt(1);
            }
        }

        return sum;
    }

    public static List<T_Measurement> getLast30DaysMeasurements(Connection conn, PreparedStatement ps, ResultSet rs, int sensorID) throws SQLException {
        // SQL Definition
        //Spravit select pre vytahovanie najnovsieho zaznamu pre kazdy den
        /*ps = conn.prepareStatement(
                "SELECT * FROM (SELECT * FROM " + T_Measurement.DBTABLE_NAME +
                " WHERE SensorID = ? AND MeasuredAt > ? ORDER BY ID ASC) AS x GROUP BY MeasuredAt"
        );*/
        ps = conn.prepareStatement(
                "SELECT * FROM " + T_Measurement.DBTABLE_NAME +
                " WHERE SensorID = ? AND MeasuredAt > ?"
        );
        int col = 0;
        Date date = Date.valueOf(LocalDate.now().minusDays(30));
        ps.setInt(++col, sensorID);
        ps.setDate(++col, date);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Measurement> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Measurements.FillEntity(rs));
            }
        }

        return arr;
    }

    public static Integer getAccumulatedValueOf30DaysAgo(Connection conn, PreparedStatement ps, ResultSet rs, int sensorID) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT AccumulatedValue FROM " + T_Measurement.DBTABLE_NAME +
                        " WHERE SensorID = ? AND MeasuredAt <= ? ORDER BY MeasuredAt DESC, AccumulatedValue DESC LIMIT 1"
        );
        int col = 0;
        Date date = Date.valueOf(LocalDate.now().minusDays(30));
        ps.setInt(++col, sensorID);
        ps.setDate(++col, date);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        int accValue = 0;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
            return 0;
        } else {
            while (rs.next()) {
                accValue = rs.getInt(1);
            }
        }

        return accValue;
    }


    // Privates
    public static T_Measurement FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Measurement.DBNAME_VALUE, rs.getInt(T_Measurement.DBNAME_VALUE));
        dict.put(T_Measurement.DBNAME_REQUESTNO, rs.getInt(T_Measurement.DBNAME_REQUESTNO));
        dict.put(T_Measurement.DBNAME_MEASUREDAT, rs.getDate(T_Measurement.DBNAME_MEASUREDAT));
        dict.put(T_Measurement.DBNAME_ACCUMULATEDVALUE, rs.getInt(T_Measurement.DBNAME_ACCUMULATEDVALUE));
        dict.put(T_Measurement.DBNAME_SENSOR_ID, rs.getInt(T_Measurement.DBNAME_SENSOR_ID));

        return T_Measurement.CreateFromRetrieved(rs.getInt(T_Measurement.DBNAME_ID), dict);
    }
}
