package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_Sensor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Sensor {

    public static int insert(Connection conn, PreparedStatement ps, T_Sensor ts) throws SQLException {
        if (ts.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Sensor is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Sensor.DBTABLE_NAME + "(" +
                        "Input, Name, SensorTypeID, ControllerUnitID" +
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
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Sensor into db failed.");

        return affectedRows;
    }

    public static T_Sensor retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

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
        rs = ps.executeQuery();
        T_Sensor ts = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            Dictionary dict = new Hashtable();
            dict.put(T_Sensor.DBNAME_INPUT, rs.getString(T_Sensor.DBNAME_INPUT));
            dict.put(T_Sensor.DBNAME_NAME, rs.getString(T_Sensor.DBNAME_NAME));
            dict.put(T_Sensor.DBNAME_SENSORTYPE_ID, rs.getInt(T_Sensor.DBNAME_SENSORTYPE_ID));
            dict.put(T_Sensor.DBNAME_CONTROLLERUNIT_ID, rs.getInt(T_Sensor.DBNAME_CONTROLLERUNIT_ID));

            ts = T_Sensor.CreateFromRetrieved(id, dict);
        }

        return ts;
    }

    ////////////////////////
    ////     Searches   ////
    ///////////////////////

    public static int retrieve_ID_by_SensorIO(Connection conn, PreparedStatement ps, ResultSet rs, String sensorIO) throws SQLException {
        Assurance.IsVarcharOk(sensorIO);

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
        rs = ps.executeQuery();
        T_Sensor ts = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            return rs.getInt(T_Sensor.DBNAME_ID);
        }

        throw new SQLException("No such sensor SensorIO" + sensorIO + " in database");
    }
}
