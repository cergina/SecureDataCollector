package Model.Database.Interaction;

import Control.ConfigClass;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.SqlConnectionOneTimeReestablisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class I_Version {
    public static boolean isCodeSqlVersionMatchingDbSQLVersion(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        // SQL Definition
        ps = conn.prepareStatement(
                "SELECT " +
                        "* " +
                        "FROM version " +
                        "ORDER BY ID DESC LIMIT 1"
        );

        int col = 0;

        // SQL Execution
        SqlConnectionOneTimeReestablisher scotr = new SqlConnectionOneTimeReestablisher();
        rs = scotr.TryQueryFirstTime(conn, ps, rs);

        // Find out if the db is not so old that there is not even a table for that
        if (!rs.isBeforeFirst()) {
            CustomLogs.Error("Could not determine version of SQL used in database. There has to be a table specific for it which came in version 12");
            return false;
        }

        // Check which TimakCommonFiles.DB VX version is in use
        rs.next();

        int dbCreationSqlVersion = rs.getInt("VERSION");
        if (dbCreationSqlVersion != ConfigClass.CODE_SUPPOSED_TO_WORK_WITH_SQL_VERSION) {
            CustomLogs.Error("You are using version " + dbCreationSqlVersion + " but this code is supposed to work with sql of version " + ConfigClass.CODE_SUPPOSED_TO_WORK_WITH_SQL_VERSION);
            return false;
        }

        // versions should match
        return true;
    }
}
