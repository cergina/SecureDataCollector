package View.Web.Servlets;

import Control.ConfigClass;
import Control.Connect.DbProvider;

import javax.servlet.http.HttpServlet;

public abstract class ConnectionServlet extends HttpServlet {

    protected static final boolean trueIfRunningRemotely = ConfigClass.RUNNING_ON_SERVER;
    protected DbProvider dbProvider;

    public void init (){
        dbProvider = new DbProvider();
    }

    public void destroy() {
        dbProvider.disconnect();
    }
}
