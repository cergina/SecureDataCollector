package Database.Interaction.Entities;

import Database.Enums.E_SensorType;
import Database.Support.Assurance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class SensorType {
    public static int insert(Connection conn, PreparedStatement ps, E_SensorType es) throws SQLException {
        if (es.IsEnumTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute E_SensorType is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        E_SensorType.DBTABLE_NAME + "(" +
                        "Name, MeasuredIn, CommTypeID" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, es.getA_Name());
        ps.setString(++col, es.getA_MeasuredIn());
        ps.setInt(++col, es.getA_CommTypeID());

        // SQL Execution
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of SensorType into db failed.");

        return affectedRows;
    }

    public static E_SensorType retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_SensorType.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        rs = ps.executeQuery();
        E_SensorType est = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            Dictionary dict = new Hashtable();
            dict.put(E_SensorType.DBNAME_NAME, rs.getString(E_SensorType.DBNAME_NAME));
            dict.put(E_SensorType.DBNAME_MEASUREDIN, rs.getString(E_SensorType.DBNAME_MEASUREDIN));
            dict.put(E_SensorType.DBNAME_COMMTYPE_ID, rs.getInt(E_SensorType.DBNAME_COMMTYPE_ID));


            est = E_SensorType.CreateFromRetrieved(id, dict);
        }

        return est;
    }
}
