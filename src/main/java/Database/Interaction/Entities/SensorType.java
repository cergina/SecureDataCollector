package Database.Interaction.Entities;

import Database.Enums.E_SensorType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        throw new NotImplementedException();
    }
}
