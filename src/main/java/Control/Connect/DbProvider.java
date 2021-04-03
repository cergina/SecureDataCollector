package Control.Connect;

import Model.Database.Interaction.I_Version;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.DbConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbProvider {
    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private boolean isItTransaction = false;
    // PUBLIC

    public DbProvider() {
        initEverything();
    }


    /***
     * Needs to be run before SQL script execution
     * The reason is that the connection gets closed sometimes on the web when using
     * from thymeleaf (servlets classical work)
     *
     *
     * @param willItBeTransaction if the sql is transaction (using inserts) put true here
     */
    public void beforeSqlExecution(boolean willItBeTransaction) {
        checkAndRestartConnectionIfRequired();

        try {
            if (willItBeTransaction) {
                conn.setAutoCommit(false);
                isItTransaction = willItBeTransaction;
            }
        } catch (Exception e) {
            afterExceptionInSqlExecution(e);
            return;
        }
    }

    public void afterOkSqlExecution() {
        afterSqlExecution(true);
    }

    /**
     * Finish transaction
     * @param exceptionToLog
     */
    public void afterExceptionInSqlExecution(Exception exceptionToLog) {
        CustomLogs.Error(exceptionToLog.getMessage());
        afterSqlExecution(false);
    }

    public void disconnect() {
        try {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
            if (ctx != null)
                ctx.close();
        }
        catch (SQLException se) {
            CustomLogs.Error("SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.Error("NamingException: " + ne.getMessage());
        }
    }

    public Connection getConn() {
        return conn;
    }

    public PreparedStatement getPs() {
        return ps;
    }

    public ResultSet getRs() {
        return rs;
    }

    // PRIVATE

    /**
     * Finish transaction
     * @param successful commit?
     */
    private void afterSqlExecution(boolean successful) {
        try {
            if (successful) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());
        } finally {
            isItTransaction = false;
            //disconnect();
        }
    }

    private void checkAndRestartConnectionIfRequired() {
        try {
            if (ctx == null || ds == null || conn == null || conn.isClosed()) {
                CustomLogs.Error("It's required to restart connection.");
                disconnect();
                initEverything();
            }
        } catch (SQLException sqle) {
                CustomLogs.Error("Restart process of connection failed");
        }
    }

    /***
     * 3 scenarios
     * - it falls (try to reestablish)
     * - version no match (no continuation in code execution
     * - alles ok
     * @return
     * @throws IOException
     */
    public boolean testConnection() throws IOException {
        try {
            if (I_Version.isCodeSqlVersionMatchingDbSQLVersion(conn, ps, rs) == false) {
                CustomLogs.Error("The version's of SQL does not match. WILL NOT ALLOW TO USE CODE.");
                throw new IOException("The version's of SQL does not match. WILL NOT ALLOW TO USE CODE.");
            }

            return true;
        } catch (SQLException sqle) {
            CustomLogs.Error("Validation of connection by SQL execution failed attempting. Attempting reestablishment.");
            return false;
        }
    }

    private void initEverything() {
        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup(DbConfig.DS_CONTEXT_NAME);
            conn = ds.getConnection();
        }
        catch (SQLException se) {
            CustomLogs.Error("SQLException: " + se.getMessage());
        }
        catch (NamingException ne) {
            CustomLogs.Error("NamingException: " + ne.getMessage());
        }
    }
}
