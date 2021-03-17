package Model.Database.Support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConnectionOneTimeReestablisher {
    // publics
    public ResultSet TryQueryFirstTime(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        try {
            return rs = ps.executeQuery();
        } catch (SQLException sqlException) {
            CustomLogs.Debug("Connection closed, attempting query second time.");
            return TryQuerySecondTime(conn, ps, rs);
        }
    }

    public int TryUpdateFirstTime(Connection conn, PreparedStatement ps) throws SQLException {
        try {
            return ps.executeUpdate();
        } catch (SQLException sqlException) {
            CustomLogs.Debug("Connection closed, attempting update second time.");
            return TryUpdateSecondTime(conn, ps);
        }
    }

    // privates
    private ResultSet TryQuerySecondTime(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        rs = ps.executeQuery();
        CustomLogs.Debug("Connection reestablished 2nd time, data should be returned ok.");

        return rs;
    }

    private int TryUpdateSecondTime(Connection conn, PreparedStatement ps) throws SQLException {
        int ar = ps.executeUpdate();
        CustomLogs.Debug("Connection reestablished 2nd time, data should be returned ok.");

        return ar;
    }
}
