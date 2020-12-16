/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_Project;
import Database.Tables.T_User;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Project {
    /***
     * Attempt to insert the parsed argument T_Project into the real database
     * @param conn
     * @param tp
     * @throws SQLException
     */
    public static int insert(Connection conn, PreparedStatement ps, T_Project tp) throws SQLException {
        if (tp.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Project is not ok for database enter");

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Project.DBTABLE_NAME + "(" +
                        "Name, CreatedAt" +
                        ") " +
                        "VALUES (" +
                        "?, CURDATE()" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tp.getA_name());

        // SQL Execution
        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Project into db failed.");

        return affectedRows;
    }

    /****
     *
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */
    public static T_Project retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.IdCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "ID, Name, CreatedAt, DeletedAt " +
                        "FROM " + T_Project.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        rs = ps.executeQuery();
        T_Project tp = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            Dictionary tmpDict = new Hashtable();
            tmpDict.put(T_Project.DBNAME_NAME, rs.getString(T_Project.DBNAME_NAME));
            tmpDict.put(T_Project.DBNAME_CreatedAt, rs.getDate(T_Project.DBNAME_CreatedAt));
            tmpDict.put(T_Project.DBNAME_DeletedAt, rs.getDate(T_Project.DBNAME_DeletedAt));

            tp = T_Project.CreateFromRetrieved(rs.getInt("ID"), tmpDict);
        }

        return tp;
    }
}
