package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_ControllerUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class ControllerUnit {

    public static int insert(Connection conn, PreparedStatement ps, T_ControllerUnit tc) throws SQLException {
        if (tc.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_ControllerUnit is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_ControllerUnit.DBTABLE_NAME + "(" +
                        "Uid, DipAddress, Zwave, CentralUnitID, FlatID" +
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
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of ControllerUnit into db failed.");

        return affectedRows;
    }

    public static T_ControllerUnit retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

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
        rs = ps.executeQuery();
        T_ControllerUnit tc = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            Dictionary dict = new Hashtable();
            dict.put(T_ControllerUnit.DBNAME_UID, rs.getInt(T_ControllerUnit.DBNAME_UID));
            dict.put(T_ControllerUnit.DBNAME_DIPADDRESS, rs.getString(T_ControllerUnit.DBNAME_DIPADDRESS));
            dict.put(T_ControllerUnit.DBNAME_ZWAVE, rs.getString(T_ControllerUnit.DBNAME_ZWAVE));
            dict.put(T_ControllerUnit.DBNAME_CENTRALUNIT_ID, rs.getInt(T_ControllerUnit.DBNAME_CENTRALUNIT_ID));
            dict.put(T_ControllerUnit.DBNAME_FLAT_ID, rs.getInt(T_ControllerUnit.DBNAME_FLAT_ID));

            tc = T_ControllerUnit.CreateFromRetrieved(id, dict);
        }

        return tc;
    }
}
