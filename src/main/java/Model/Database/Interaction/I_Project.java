/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Project;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_Project extends InteractionWithDatabase {
    /***
     * Attempt to insert the parsed argument T_Project into the real database
     * @param conn
     * @param tp
     * @throws SQLException
     */
    public static int insert(Connection conn, PreparedStatement ps, T_Project tp) throws SQLException {
        if (tp.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Project is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_Project.DBNAME_NAME, T_Project.DBNAME_CreatedAt
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Project.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, CURDATE()" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tp.getA_Name());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Project into db failed.");

        return affectedRows;
    }


    public static T_Project retrieveByName(Connection conn, PreparedStatement ps, ResultSet rs, String projectName) throws SQLException {
        Assurance.varcharCheck(projectName);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "ID, Name, CreatedAt, DeletedAt " +
                        "FROM " + T_Project.DBTABLE_NAME + " " +
                        "WHERE Name=?"
        );

        int col = 0;
        ps.setString(++col, projectName);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Project tp = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tp = I_Project.FillEntity(rs);
        }

        return tp;
    }


    // Privates
    public static T_Project FillEntity(ResultSet rs) throws SQLException {

        Dictionary tmpDict = new Hashtable();

        tmpDict.put(T_Project.DBNAME_NAME, rs.getString(T_Project.DBNAME_NAME));
        tmpDict.put(T_Project.DBNAME_CreatedAt, rs.getDate(T_Project.DBNAME_CreatedAt));

        Date deletedAt = rs.getDate(T_Project.DBNAME_DeletedAt);
        if (deletedAt != null)
            tmpDict.put(T_Project.DBNAME_DeletedAt, deletedAt);

        return T_Project.CreateFromRetrieved(rs.getInt(T_Project.DBNAME_ID), tmpDict);
    }
}
