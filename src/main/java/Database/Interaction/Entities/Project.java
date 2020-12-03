/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Tables.T_Project;

import java.sql.*;

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
                        "project (" +
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
            throw new SQLException("Something happened. Insertion of project into db failed.");

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
                        "FROM project " +
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

            tp = T_Project.CreateFromRetrieved(rs.getInt("ID"),
                    rs.getString("Name"),
                    rs.getDate("CreatedAt"),
                    rs.getDate("DeletedAt")
            );
        }

        return tp;
    }
}
