package com.collector.web;

import Control.ConfigClass;
import Database.Interaction.Entities.Address;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Tables.T_Address;
import org.json.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionServlet extends HttpServlet {

    static final String SITE_URL =
            ConfigClass.RUNNING_ON_SERVER ?
                    "/dcs" :
                    "/SecureDataCollector-1.0-SNAPSHOT";

    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    ArrayList<T_Address> retrieveAllAddress() {
        try {
            return Address.retrieveAll(conn, ps, rs);

        } catch (SQLException e) {
            CustomLogs.Error(e.getMessage());
        }
        return null;
    }

    public void init (){
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

    public void destroy() {
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
}
