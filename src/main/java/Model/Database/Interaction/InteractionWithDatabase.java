package Model.Database.Interaction;

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
            CustomLogs.Debug("Nothing was returned");
        } else {
            while (rs.next()) {
                CustomLogs.Debug("Filling entity");
                arr.add((T)specificTypeObj.FillEntityFromResultSet(rs));
                //arr.add(I_Address.FillEntity(rs));
            }
        }

        CustomLogs.Debug("Exiting retrieveAll");
        return arr;
    }


    /*
     * This will get you back the PRIMARY KEY value of the last row that you inserted, because it's per connection !
     */
    public static int retrieveLatestPerConnectionInsertedID(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        int latest  = -1;

        ps = conn.prepareStatement(
                "SELECT LAST_INSERT_ID();"
        );

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        if (!rs.isBeforeFirst()) {
            /* nothing was returned */
        } else {
            rs.next();

            latest = rs.getInt(1);
        }

        return latest;
    }

}
