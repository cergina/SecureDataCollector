package com.collector.web.servlets;

import Control.ConfigClass;
import Database.Interaction.Entities.Address;
import Database.Support.CustomLogs;
import Database.Support.DbConfig;
import Database.Tables.T_Address;
import com.collector.db.DbProvider;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class ConnectionServlet extends HttpServlet {

    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;

    DbProvider dbProvider;

    public void init (){
        dbProvider = new DbProvider();
    }

    public void destroy() {
        dbProvider.disconnect();
    }
}
