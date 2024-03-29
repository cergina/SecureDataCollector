package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.E_SensorType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

public class I_SensorType extends InteractionWithDatabase {
    public static int insert(Connection conn, PreparedStatement ps, E_SensorType es) throws SQLException {
        if (es.IsEnumTableOkForDatabaseEnter() == false)
            throw new SQLException("Given attribute E_SensorType is not ok for database enter");

        // Fill SQL db table names
        String tableNames = String.join(", ",
                    E_SensorType.DBNAME_NAME, E_SensorType.DBNAME_MEASUREDIN, E_SensorType.DBNAME_COMMTYPE_ID
                );

        // SQL Definition
        ps = conn.prepareStatement(
                "INSERT INTO " +
                        E_SensorType.DBTABLE_NAME + "(" +
                        tableNames +
                        ") " +
                        "VALUES (" +
                        "?, ?, ?" +
                        ") "
        );

        int col = 0;
        ps.setString(++col, es.getA_Name());
        ps.setString(++col, es.getA_MeasuredIn());
        ps.setInt(++col, es.getA_CommTypeID());

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        int affectedRows = scotr.TryUpdateFirstTime(conn, ps);

        if (affectedRows == 0)
            throw new SQLException("Something happened. Insertion of SensorType into db failed.");

        return affectedRows;
    }

    public static E_SensorType retrieveByName(Connection conn, PreparedStatement ps, ResultSet rs, String name) throws SQLException {
        Assurance.varcharCheck(name);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + E_SensorType.DBTABLE_NAME + " " +
                        "WHERE " + E_SensorType.DBNAME_NAME + "=?"
        );

        int col = 0;
        ps.setString(++col, name);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        E_SensorType st = null;

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            st = I_SensorType.FillEntity(rs);
        }

        return st;
    }

    // Privates
    public static E_SensorType FillEntity(ResultSet rs) throws SQLException {

        Dictionary dict = new Hashtable();

        dict.put(E_SensorType.DBNAME_NAME, rs.getString(E_SensorType.DBNAME_NAME));
        dict.put(E_SensorType.DBNAME_MEASUREDIN, rs.getString(E_SensorType.DBNAME_MEASUREDIN));
        dict.put(E_SensorType.DBNAME_COMMTYPE_ID, rs.getInt(E_SensorType.DBNAME_COMMTYPE_ID));


        return E_SensorType.CreateFromRetrieved(rs.getInt(E_SensorType.DBNAME_ID), dict);
    }
}
