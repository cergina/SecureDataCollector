/*
SONET SLOVAKIA - Secure Data Collector
@author: github.com/cergina
2020-2021
 */

package Database.Interaction.Entities;

import Database.Support.Assurance;
import Database.Support.SqlConnectionOneTimeReestablisher;
import Database.Tables.T_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

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
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Project tp = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            tp = Project.FillEntity(rs);
        }

        return tp;
    }

    /*****
     *
     * @param conn
     * @param ps
     * @param rs
     * @return
     * @throws SQLException
     */
    public static ArrayList<T_Project> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "ID, Name, CreatedAt, DeletedAt " +
                        "FROM " + T_Project.DBTABLE_NAME + " " +
                        "ORDER BY ID asc"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        ArrayList<T_Project> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(Project.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    private static T_Project FillEntity(ResultSet rs) throws SQLException {
        T_Project t = null;

        Dictionary tmpDict = new Hashtable();
        tmpDict.put(T_Project.DBNAME_NAME, rs.getString(T_Project.DBNAME_NAME));
        tmpDict.put(T_Project.DBNAME_CreatedAt, rs.getDate(T_Project.DBNAME_CreatedAt));

        t = T_Project.CreateFromRetrieved(rs.getInt(T_Project.DBNAME_ID), tmpDict, rs.getDate(T_Project.DBNAME_DeletedAt));

        return t;
    }
}
