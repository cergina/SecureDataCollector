package com.collector.web;

import Database.Interaction.Entities.Address;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Support.ServletHelper;
import Database.Tables.T_Address;
import com.collector.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "IndexServlet", urlPatterns = IndexServlet.SERVLET_URL)
public class IndexServlet extends HttpServlet {
    public static final String SERVLET_URL =  "/";
    public static final String SITE_NAME = "index.html";

    private InitialContext ctx = null;
    private DataSource ds = null;
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        try {
            ArrayList<T_Address> arr = Address.retrieveAll(conn, ps, rs);

            context.setVariable(T_Address.DBNAME_COUNTRY, "" + arr.get(0).getA_Country());
            context.setVariable(T_Address.DBNAME_CITY, "" + arr.get(0).getA_City());
            context.setVariable(T_Address.DBNAME_STREET, "" + arr.get(0).getA_Street());
            context.setVariable(T_Address.DBNAME_HOUSENO, "" + arr.get(0).getA_HouseNO());
            context.setVariable(T_Address.DBNAME_ZIP, "" + arr.get(0).getA_ZIP());

            engine.process(SITE_NAME, context, response.getWriter());

        } catch (SQLException e) {
            ServletHelper.Send404(response);

            CustomLogs.Error(e.getMessage());
        }
    }

    // GENERIC, has to be in every Servlet class, abstract, or extend does not work, tried
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