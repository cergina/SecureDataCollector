package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_CentralUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

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
                        T_CentralUnit.DBTABLE_NAME + "(" +
                        "Uid, FriendlyName, SimNO, Imei, Zwave, ProjectID, AddressID" +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?, ?, ?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tcu.getA_Uid());
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
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_CentralUnit.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        rs = ps.executeQuery();
        T_CentralUnit tc = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            Dictionary dict = new Hashtable();
            dict.put(T_CentralUnit.DBNAME_UID, rs.getInt(T_CentralUnit.DBNAME_UID));
            dict.put(T_CentralUnit.DBNAME_FRIENDLYNAME, rs.getString(T_CentralUnit.DBNAME_FRIENDLYNAME));
            dict.put(T_CentralUnit.DBNAME_SIMNO, rs.getString(T_CentralUnit.DBNAME_SIMNO));
            dict.put(T_CentralUnit.DBNAME_IMEI, rs.getString(T_CentralUnit.DBNAME_IMEI));
            dict.put(T_CentralUnit.DBNAME_ZWAVE, rs.getString(T_CentralUnit.DBNAME_ZWAVE));
            dict.put(T_CentralUnit.DBNAME_PROJECT_ID, rs.getInt(T_CentralUnit.DBNAME_PROJECT_ID));
            dict.put(T_CentralUnit.DBNAME_ADDRESS_ID, rs.getInt(T_CentralUnit.DBNAME_ADDRESS_ID));


            tc = T_CentralUnit.CreateFromRetrieved(id, dict);
        }

        return tc;
    }
}
