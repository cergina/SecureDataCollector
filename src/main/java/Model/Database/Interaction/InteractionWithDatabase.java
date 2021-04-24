package Model.Database.Interaction;

import Model.Database.Support.Assurance;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;
import Model.Database.Tables.DbEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class InteractionWithDatabase {
    public static <T extends DbEntity> T retrieve(Connection conn, PreparedStatement ps, ResultSet rs, T specificTypeObj, int id) throws SQLException {
        CustomLogs.Debug("Entering retrieve");

        Assurance.idCheck(id);

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + specificTypeObj.GetDbTableName() + " " +
                        "WHERE ID=?"
        );

        ps.setInt(1, id);

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
            return null;
        }

        rs.next();

        return (T) specificTypeObj.FillEntityFromResultSet(rs);
    }

    public static <T extends DbEntity> List<T> retrieveAll(Connection conn, PreparedStatement ps, ResultSet rs, T specificTypeObj) throws SQLException {
        CustomLogs.Debug("Entering retrieveAll");

        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM " + specificTypeObj.GetDbTableName() + " " +
                        "ORDER BY ID asc"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        List<T> arr = new ArrayList<>();

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
            return arr;
        }

        // Fill and return filled array
        while (rs.next()) {
            arr.add((T) specificTypeObj.FillEntityFromResultSet(rs));
        }

        return arr;
    }


    /*
     * This will get you back the PRIMARY KEY value of the last row that you inserted, because it's per connection !
     */
    public static int retrieveLatestPerConnectionInsertedID(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        ps = conn.prepareStatement(
                "SELECT LAST_INSERT_ID();"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
            return -1;
        }

        // get id and return it
        rs.next();

        return rs.getInt(1);
    }

}
