package com.collector.web.servlet;

import Control.ConfigClass;
import com.collector.db.DbProvider;

import javax.servlet.http.HttpServlet;

public abstract class ConnectionServlet extends HttpServlet {

    DbProvider dbProvider;

    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;

    public void init (){
        dbProvider = new DbProvider();
    }

    public void destroy() {
        dbProvider.disconnect();
    }
}
