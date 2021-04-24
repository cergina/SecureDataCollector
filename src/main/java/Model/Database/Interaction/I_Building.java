package Model.Database.Interaction;

import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Building;
import Model.Database.Tables.T_Flat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class I_Building extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, T_Building tf) throws SQLException {
        if (tf.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Building is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                T_Building.DBNAME_PROJECT_ID, T_Building.DBNAME_ADDRESS_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Building.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tf.getA_ProjectID());
        ps.setInt(++col, tf.getA_AddressID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of Building into db failed.");

        return affectedRows;
    }

    public static List<T_Building> retrieveByProjectId(Connection conn, PreparedStatement ps, ResultSet rs, int projectId) throws SQLException {
        Assurance.idCheck(projectId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Building.DBTABLE_NAME + " " +
                        "WHERE " + T_Building.DBNAME_PROJECT_ID + "=?"
        );

        int col = 0;
        ps.setInt(++col, projectId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Building> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            while (rs.next()) {
                arr.add(I_Building.FillEntity(rs));
            }
        }

        return arr;
    }

    // Privates
    public static T_Building FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Building.DBNAME_PROJECT_ID, rs.getInt(T_Building.DBNAME_PROJECT_ID));
        dict.put(T_Building.DBNAME_ADDRESS_ID, rs.getInt(T_Building.DBNAME_ADDRESS_ID));

        return T_Building.CreateFromRetrieved(rs.getInt(T_Building.DBNAME_ID), dict);
    }
}
