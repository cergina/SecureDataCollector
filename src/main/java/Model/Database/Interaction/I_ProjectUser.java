package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_Project_user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class I_ProjectUser extends InteractionWithDatabase {

    public static int insert(Connection conn, PreparedStatement ps, T_Project_user tpu) throws SQLException {
        if (tpu.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_Project_user is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_Project_user.DBNAME_PROJECTID, T_Project_user.DBNAME_USERID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_Project_user.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setInt(++col, tpu.getA_ProjectID());
        ps.setInt(++col, tpu.getA_UserID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of T_Project_user into db failed.");

        return affectedRows;
    }

    public static List<T_Project_user> retrieveAllForUser(Connection conn, PreparedStatement ps, ResultSet rs, int userId) throws SQLException {
        Assurance.idCheck(userId);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Project_user.DBTABLE_NAME + " " +
                        "WHERE UserID=? ORDER BY ID asc"
        );

        int col = 0;
        ps.setInt(++col, userId);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T_Project_user> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {

            while (rs.next()) {
                arr.add(FillEntity(rs));
            }

        }

        return arr;
    }

    /* useless ? */
    public static T_Project_user retrieve(Connection conn, PreparedStatement ps, ResultSet rs, int id) throws SQLException {
        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + T_Project_user.DBTABLE_NAME + " " +
                        "WHERE ID=?"
        );

        int col = 0;
        ps.setInt(++col, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        T_Project_user t = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            t = FillEntity(rs);
        }

        return t;
    }


    public static T_Project_user FillEntityFromExternal(ResultSet rs) throws SQLException {
        return FillEntity(rs);
    }

    // Privates
    public static T_Project_user FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_Project_user.DBNAME_PROJECTID, rs.getInt(T_Project_user.DBNAME_PROJECTID));
        dict.put(T_Project_user.DBNAME_USERID, rs.getInt(T_Project_user.DBNAME_USERID));


        return T_Project_user.CreateFromRetrieved(rs.getInt(T_Project_user.DBNAME_ID), dict);
    }
}
