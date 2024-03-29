package Model.Database.Interaction;

import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.T_TestLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_TestLogs extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, T_TestLog tt) throws SQLException {
        if (tt.IsTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute T_TestLog is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    T_TestLog.DBNAME_EVENT, T_TestLog.DBNAME_BODY
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        T_TestLog.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, tt.getA_Event());
        ps.setString(++col, tt.getA_Body());


        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of TestLog into db failed.");

        return affectedRows;
    }



    // Privates
    public static T_TestLog FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(T_TestLog.DBNAME_EVENT, rs.getString(T_TestLog.DBNAME_EVENT));
        dict.put(T_TestLog.DBNAME_BODY, rs.getString(T_TestLog.DBNAME_BODY));

        return T_TestLog.CreateFromRetrieved(rs.getInt(T_TestLog.DBNAME_ID), dict);
    }
}
