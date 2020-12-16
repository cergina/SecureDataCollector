package Database.Interaction.Entities;

import Database.Tables.T_Flat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Flat {
    public static int insert(Connection conn, PreparedStatement ps, T_Flat tf) throws SQLException {
        if (tf.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Flat is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        "flat (" +
                        "ApartmentNO, AddressID" +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        //ps.setString(++col, tf.);
        //ps.setInt(++col, tf.);

        // SQL Execution
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of flat into db failed.");

        return affectedRows;
    }
}
