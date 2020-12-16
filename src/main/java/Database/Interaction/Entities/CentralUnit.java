package Database.Interaction.Entities;

import Database.Tables.T_CentralUnit;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CentralUnit {
    /****
     * Attempt to insert the parsed argument T_CentralUnit into the real database
     * @param conn
     * @param ps
     * @param tcu
     * @return
     * @throws SQLException
     */
    public static int insert(Connection conn, PreparedStatement ps, T_CentralUnit tcu) throws SQLException {
        if (tcu.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_CentralUnit is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        "centralunit (" +
                        "Uid, FriendlyName, SimNO, Imei, Zwave, ProjectID, AddressID" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tcu.getA_Uid());
        ps.setString(++col, tcu.getA_FriendlyName());
        ps.setString(++col, tcu.getA_SimNO());
        ps.setString(++col, tcu.getA_Imei());
        ps.setString(++col, tcu.getA_Zwave());
        ps.setInt(++col, tcu.getA_ProjectID());
        ps.setInt(++col, tcu.getA_AddressID());

        // SQL Execution
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of CentralUnit into db failed.");

        return affectedRows;
    }

    /****
     *
     * @param conn
     * @param ps
     * @param rs
     * @param id
     * @return
     * @throws SQLException
     */
    public static T_CentralUnit retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        throw new NotImplementedException();
    }
}
