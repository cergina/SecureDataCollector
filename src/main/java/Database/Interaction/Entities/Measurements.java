package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_Measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Measurements {

    public static int insert(Connection conn, PreparedStatement ps, T_Measurement tm) throws SQLException {
        if (tm.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Measurements is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Measurement.DBTABLE_NAME + "(" +
                        "Value, MeasuredIncrement, MeasuredAt, SensorID" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tm.getA_Value());
        ps.setInt(++col, tm.getA_MeasuredIncrement());
        ps.setDate(++col, tm.getA_MeasuredAt());
        ps.setInt(++col, tm.getA_SensorID());

        // SQL Execution
        int affectedRows = ps.executeUpdate();

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
        rs = ps.executeQuery();
        T_Measurement tm = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            Dictionary dict = new Hashtable();
            dict.put(T_Measurement.DBNAME_VALUE, rs.getInt(T_Measurement.DBNAME_VALUE));
            dict.put(T_Measurement.DBNAME_MEASUREDINCREMENT, rs.getInt(T_Measurement.DBNAME_MEASUREDINCREMENT));
            dict.put(T_Measurement.DBNAME_MEASUREDAT, rs.getDate(T_Measurement.DBNAME_MEASUREDAT));
            dict.put(T_Measurement.DBNAME_SENSOR_ID, rs.getInt(T_Measurement.DBNAME_SENSOR_ID));

            tm = T_Measurement.CreateFromRetrieved(id, dict);
        }

        return tm;
    }
}
