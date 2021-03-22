package Control.Connect;

import Model.Database.Support.CustomLogs;
import Model.Database.Support.DbConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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

    public DbProvider() {
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

    public void beforeSqlExecution() {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());
        }
    }

    public void afterSuccessfulSqlExecution() {
        try {
            conn.commit();
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());
        }
    }

    public void transactionRollback() {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            CustomLogs.Error(e.getMessage());
        }
    }

    public void restartAutoCommit() {
        try {
            getConn().setAutoCommit(true);
        } catch (Exception exception) {
            CustomLogs.Error(exception.getMessage());
        }
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
}
